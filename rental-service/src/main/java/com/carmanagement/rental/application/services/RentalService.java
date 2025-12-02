package com.carmanagement.rental.application.services;

import com.carmanagement.rental.domain.models.Rental;
import com.carmanagement.rental.domain.models.RentalStatus;
import com.carmanagement.rental.domain.ports.RentalRepositoryPort;
import com.carmanagement.rental.infrastructure.clients.AuthServiceClient;
import com.carmanagement.rental.infrastructure.clients.CarServiceClient;
import com.carmanagement.rental.shared.exceptions.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RentalService {

    private static final Logger logger = LoggerFactory.getLogger(RentalService.class);

    private final RentalRepositoryPort rentalRepository;
    private final AuthServiceClient authServiceClient;
    private final CarServiceClient carServiceClient;

    // Configuration from application.yml
    @Value("${rental.penalties.late-fee-percentage:10}")
    private double lateFeePercentage;

    @Value("${rental.penalties.max-penalty-percentage:50}")
    private double maxPenaltyPercentage;

    @Value("${rental.penalties.grace-period-hours:2}")
    private int gracePeriodHours;

    // Simple cache for car prices (in production, use Redis or caffeine)
    private final Map<Long, BigDecimal> carPriceCache = new ConcurrentHashMap<>();

    public RentalService(RentalRepositoryPort rentalRepository,
                         AuthServiceClient authServiceClient,
                         CarServiceClient carServiceClient) {
        this.rentalRepository = rentalRepository;
        this.authServiceClient = authServiceClient;
        this.carServiceClient = carServiceClient;
    }

    @CircuitBreaker(name = "rentalService", fallbackMethod = "createRentalFallback")
    @Transactional
    public Rental createRental(Long userId, Long carId,
                               LocalDateTime startDate, LocalDateTime endDate,
                               String authToken) {

        logger.info("Creating rental for user: {}, car: {}, dates: {} to {}",
                userId, carId, startDate, endDate);

        // 1. Validate user exists
        validateUser(userId, authToken);

        // 2. Validate car exists and is available
        validateCarAvailability(carId, startDate, endDate, authToken);

        // 3. Get current daily price AND calculate total price
        BigDecimal dailyPrice = getCarPrice(carId, authToken);  // Get current price
        BigDecimal totalPrice = calculatePrice(dailyPrice, startDate, endDate);

        // 4. Create rental WITH daily price stored
        Rental rental = new Rental(userId, carId, startDate, endDate, totalPrice, dailyPrice);
        rental.setStatus(RentalStatus.CONFIRMED);

        // 5. Mark car as rented
        markCarAsRented(carId, authToken);

        // 6. Save rental
        return rentalRepository.save(rental);
    }

    @Transactional
    public Rental returnCar(Long rentalId, Long userId, String authToken) {
        logger.info("Processing return for rental: {}, user: {}", rentalId, userId);

        // 1. Get the rental
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));

        // 2. Verify user owns this rental
        if (!rental.getUserId().equals(userId)) {
            throw new UnauthorizedRentalAccessException(userId, rentalId);
        }

        // 3. Verify rental is active
        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new InvalidRentalOperationException("return car",
                    "Rental is not active. Current status: " + rental.getStatus());
        }

        // 4. Calculate late penalty if any
        LocalDateTime actualReturnDate = LocalDateTime.now();
        rental.setActualReturnDate(actualReturnDate);

        // Check if within grace period
        LocalDateTime dueDateWithGrace = rental.getEndDate().plusHours(gracePeriodHours);

        if (actualReturnDate.isAfter(dueDateWithGrace)) {
            BigDecimal penalty = calculateLatePenalty(rental, actualReturnDate, authToken);
            rental.setLatePenalty(penalty);
            rental.setStatus(RentalStatus.OVERDUE);
            logger.warn("Late return penalty applied: {}", penalty);
        } else {
            rental.setStatus(RentalStatus.COMPLETED);
            logger.info("Car returned on time (within grace period)");
        }

        // 5. Mark car as available
        markCarAsAvailable(rental.getCarId(), authToken);

        // 6. Clear cached price (car might have new price now)
        carPriceCache.remove(rental.getCarId());

        // 7. Save updated rental
        return rentalRepository.save(rental);
    }

    private BigDecimal calculateLatePenalty(Rental rental, LocalDateTime actualReturnDate, String authToken) {
        // Calculate days late (considering grace period)
        LocalDateTime dueDateWithGrace = rental.getEndDate().plusHours(gracePeriodHours);
        long hoursLate = Duration.between(dueDateWithGrace, actualReturnDate).toHours();
        long daysLate = (hoursLate + 23) / 24;

        if (daysLate < 1) daysLate = 1; // Minimum 1 day penalty

        // Get car price
        BigDecimal dailyPrice = rental.getDailyPriceAtRental();

        if (dailyPrice == null) {
            // Fallback: get current price if not stored (for old rentals)
            logger.warn("Daily price not stored for rental {}, fetching current price", rental.getRentalId());
            dailyPrice = getCarPrice(rental.getCarId(), authToken);
        }

        // Calculate daily penalty
        BigDecimal dailyPenaltyRate = BigDecimal.valueOf(lateFeePercentage / 100.0);
        BigDecimal dailyPenalty = dailyPrice.multiply(dailyPenaltyRate);

        // Calculate total penalty
        BigDecimal totalPenalty = dailyPenalty.multiply(BigDecimal.valueOf(daysLate));

        // Apply maximum penalty cap
        BigDecimal maxAllowed = dailyPrice.multiply(BigDecimal.valueOf(maxPenaltyPercentage / 100.0));

        return totalPenalty.min(maxAllowed); // Return the smaller of the two
    }

    private BigDecimal getCarPrice(Long carId, String authToken) {
        // Check cache first
        return carPriceCache.computeIfAbsent(carId, id -> {
            // If not in cache, fetch from Car Service
            CarServiceClient.CarResponse car = carServiceClient.getCarById(id, authToken);
            return car.getDailyPrice();
        });
    }

    private void validateUser(Long userId, String authToken) {
        try {
            AuthServiceClient.UserResponse user =
                    authServiceClient.getUserById(userId, authToken);

            if (!user.isActive()) {
                throw new InvalidRentalOperationException("create rental",
                        "User account is not active");
            }

        } catch (Exception e) {
            throw new UserNotFoundException(userId);
        }
    }

    private void validateCarAvailability(Long carId, LocalDateTime startDate,
                                         LocalDateTime endDate, String authToken) {
        // Check if car exists and is available
        CarServiceClient.CarResponse car =
                carServiceClient.getCarById(carId, authToken);

        if (!car.isAvailable()) {
            throw new InvalidRentalOperationException("create rental",
                    "Car is not available for rental");
        }

        // Check for overlapping rentals
        List<Rental> overlappingRentals =
                rentalRepository.findOverlappingRentals(carId, startDate, endDate);

        if (!overlappingRentals.isEmpty()) {
            throw new InvalidRentalOperationException("create rental",
                    "Car is already booked for selected dates");
        }
    }

    private BigDecimal calculatePrice(BigDecimal dailyPrice,
                                      LocalDateTime startDate, LocalDateTime endDate) {
        long days = Duration.between(startDate, endDate).toDays();
        if (days < 1) days = 1;
        return dailyPrice.multiply(BigDecimal.valueOf(days));
    }

    private void markCarAsRented(Long carId, String authToken) {
        carServiceClient.markAsRented(carId, authToken);
    }

    private void markCarAsAvailable(Long carId, String authToken) {
        carServiceClient.markAsAvailable(carId, authToken);
    }

    // Fallback method for circuit breaker
    public Rental createRentalFallback(Long userId, Long carId,
                                       LocalDateTime startDate, LocalDateTime endDate,
                                       String authToken, Exception e) {
        logger.error("Fallback triggered for rental creation. Error: {}", e.getMessage());
        throw new RuntimeException("Unable to create rental at this time. Please try again later.");
    }

    public List<Rental> getUserRentals(Long userId) {
        return rentalRepository.findByUserId(userId);
    }

    public Rental getRentalById(Long rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));
    }
}