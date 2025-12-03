package com.carmanagement.rental.application.services;

import com.carmanagement.rental.domain.models.Rental;
import com.carmanagement.rental.domain.models.RentalStatus;
import com.carmanagement.rental.domain.ports.RentalRepositoryPort;
import com.carmanagement.rental.infrastructure.clients.AuthServiceClient;
import com.carmanagement.rental.infrastructure.clients.CarServiceClient;
import com.carmanagement.rental.shared.exceptions.*;
import com.carmanagement.rental.shared.dtos.UserResponsePath;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentalService {

    private static final Logger logger = LoggerFactory.getLogger(RentalService.class);

    private final RentalRepositoryPort rentalRepository;
    private final AuthServiceClient authServiceClient;
    private final CarServiceClient carServiceClient;

    public RentalService(RentalRepositoryPort rentalRepository,
                         AuthServiceClient authServiceClient,
                         CarServiceClient carServiceClient) {
        this.rentalRepository = rentalRepository;
        this.authServiceClient = authServiceClient;
        this.carServiceClient = carServiceClient;
    }

    // ---------------------------
    // RENTAL CREATION
    // ---------------------------
    @CircuitBreaker(name = "rentalService", fallbackMethod = "createRentalFallback")
    @Transactional
    public Rental createRental(Long userId, Long carId,
                               LocalDate startDate, LocalDate endDate,
                               String authToken) {

        logger.info("Creating rental for user {}, car {}, dates {} to {}", userId, carId, startDate, endDate);

        validateUser(userId, authToken);
        validateCarAvailability(carId, startDate, endDate, authToken);

        BigDecimal dailyPrice = getCarPrice(carId, authToken);
        BigDecimal totalPrice = calculatePrice(dailyPrice, startDate, endDate);

        Rental rental = new Rental(userId, carId, startDate, endDate, totalPrice, dailyPrice);
        rental.setStatus(RentalStatus.CONFIRMED);

        carServiceClient.markAsRented(carId, authToken);

        return rentalRepository.save(rental);
    }

    // ---------------------------
    // RENTAL RETURN
    // ---------------------------
    @Transactional
    public Rental returnRental(Long rentalId, Long userId, String authToken) {
        Rental rental = getRentalOrThrow(rentalId);

        if (!rental.getUserId().equals(userId)) {
            throw new UnauthorizedRentalAccessException(userId, rentalId);
        }
        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new InvalidRentalOperationException("return rental",
                    "Rental is not active. Current status: " + rental.getStatus());
        }

        rental.setActualReturnDate(LocalDate.now());

        if (rental.getActualReturnDate().isAfter(rental.getEndDate())) {
            rental.setStatus(RentalStatus.OVERDUE);
            rental.setLatePenalty(calculateLatePenalty(rental));
        } else {
            rental.setStatus(RentalStatus.COMPLETED);
        }

        carServiceClient.markAsAvailable(rental.getCarId(), authToken);
        return rentalRepository.save(rental);
    }

    // ---------------------------
    // RENTAL CANCELLATION
    // ---------------------------
    @Transactional
    public Rental cancelRental(Long rentalId, String authToken) {
        Rental rental = getRentalOrThrow(rentalId);

        if (rental.getStatus() != RentalStatus.CONFIRMED) {
            throw new InvalidRentalOperationException("cancel rental",
                    "Cannot cancel rental. Current status: " + rental.getStatus());
        }

        rental.setStatus(RentalStatus.CANCELLED);
        carServiceClient.markAsAvailableAfterCancel(rental.getCarId(), authToken);

        return rentalRepository.save(rental);
    }

    // ---------------------------
    // HELPERS
    // ---------------------------
    private Rental getRentalOrThrow(Long rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));
    }

    private void validateUser(Long userId, String authToken) {
        UserResponsePath user = authServiceClient.getUserById(userId, authToken);
        if (user == null) throw new UserNotFoundException(userId);
    }

    private void validateCarAvailability(Long carId, LocalDate startDate,
                                         LocalDate endDate, String authToken) {
        CarServiceClient.CarResponse car = carServiceClient.getCarById(carId, authToken);
        if (!car.isAvailable()) {
            throw new InvalidRentalOperationException("create rental", "Car is not available");
        }
        List<Rental> overlapping = rentalRepository.findOverlappingRentals(carId, startDate, endDate);
        if (!overlapping.isEmpty()) {
            throw new InvalidRentalOperationException("create rental", "Car already booked for selected dates");
        }
    }

    private BigDecimal getCarPrice(Long carId, String authToken) {
        return carServiceClient.getCarById(carId, authToken).getDailyPrice();
    }

    private BigDecimal calculatePrice(BigDecimal dailyPrice, LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days < 1) days = 1;
        return dailyPrice.multiply(BigDecimal.valueOf(days));
    }

    private BigDecimal calculateLatePenalty(Rental rental) {
        BigDecimal dailyPenaltyRate = rental.getDailyPriceAtRental().multiply(BigDecimal.valueOf(0.10)); // 10%
        long daysLate = ChronoUnit.DAYS.between(rental.getEndDate(), rental.getActualReturnDate());
        if (daysLate < 1) daysLate = 1;
        return dailyPenaltyRate.multiply(BigDecimal.valueOf(daysLate));
    }
    public CarServiceClient.AvailabilityResponse checkCarAvailability(Long carId, String token) {
        return carServiceClient.checkAvailability(carId, token);
    }


    @Transactional(readOnly = true)
    public List<Rental> getUserRentals(Long userId) {
        return rentalRepository.findByUserId(userId);
    }
    @Transactional(readOnly = true)
    public Rental getRentalById(Long rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));
    }


    // ---------------------------
    // FALLBACK
    // ---------------------------
    public Rental createRentalFallback(Long userId, Long carId,
                                       LocalDate startDate, LocalDate endDate,
                                       String authToken, Throwable e) {
        logger.error("Rental creation failed for user {}, car {}. Cause: {}", userId, carId, e.getMessage(), e);
        throw new ServiceUnavailableException("Rental Service unavailable for operation: create a new rental at this time");
    }
}
