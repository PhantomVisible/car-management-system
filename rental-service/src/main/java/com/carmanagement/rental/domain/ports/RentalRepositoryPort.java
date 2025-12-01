package com.carmanagement.rental.domain.ports;

import com.carmanagement.rental.domain.models.Rental;
import com.carmanagement.rental.domain.models.RentalStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RentalRepositoryPort {
    Rental save(Rental rental);
    Optional<Rental> findById(Long rentalId);
    List<Rental> findByUserId(Long userId);
    List<Rental> findByCarId(Long carId);
    List<Rental> findByStatus(RentalStatus status);

    // Critical for preventing double bookings!
    List<Rental> findOverlappingRentals(Long carId, LocalDateTime startDate, LocalDateTime endDate);

    List<Rental> findAll();
    boolean existsById(Long rentalId);
}