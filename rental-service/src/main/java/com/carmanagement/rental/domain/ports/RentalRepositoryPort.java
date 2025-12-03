package com.carmanagement.rental.domain.ports;

import com.carmanagement.rental.domain.models.Rental;
import com.carmanagement.rental.domain.models.RentalStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentalRepositoryPort {
    // Basic CRUD operations
    Rental save(Rental rental);
    Optional<Rental> findById(Long rentalId);
    List<Rental> findAll();

    // Custom queries
    List<Rental> findByUserId(Long userId);
    List<Rental> findByCarId(Long carId);
    List<Rental> findByStatus(RentalStatus status);
    List<Rental> findOverlappingRentals(Long carId, LocalDate startDate, LocalDate endDate);
}