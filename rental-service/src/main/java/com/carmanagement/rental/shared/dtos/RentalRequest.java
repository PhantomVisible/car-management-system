package com.carmanagement.rental.shared.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class RentalRequest {

    @NotNull(message = "Car ID is required")
    @Positive(message = "Car ID must be positive")
    private Long carId;

    @NotNull(message = "Start date is required")
    @Future(message = "Start date must be in the future")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    // Constructors
    public RentalRequest() {}

    public RentalRequest(Long carId, LocalDateTime startDate, LocalDateTime endDate) {
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
}