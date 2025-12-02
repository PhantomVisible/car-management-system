package com.carmanagement.rental.infrastructure.repositories;

import com.carmanagement.rental.domain.models.Rental;
import com.carmanagement.rental.domain.models.RentalStatus;
import com.carmanagement.rental.domain.ports.RentalRepositoryPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentalRepositoryAdapter extends JpaRepository<Rental, Long>, RentalRepositoryPort {

    List<Rental> findByUserId(Long userId);

    List<Rental> findByCarId(Long carId);

    List<Rental> findByStatus(RentalStatus status);

    @Query("SELECT r FROM Rental r WHERE " +
            "r.carId = :carId AND " +
            "r.status IN ('CONFIRMED', 'ACTIVE') AND " +
            "((r.startDate BETWEEN :startDate AND :endDate) OR " +
            "(r.endDate BETWEEN :startDate AND :endDate) OR " +
            "(r.startDate <= :startDate AND r.endDate >= :endDate))")
    List<Rental> findOverlappingRentals(
            @Param("carId") Long carId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}