package com.carmanagement.rental.shared.dtos;

import com.carmanagement.rental.domain.models.RentalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalResponse {

    private Long rentalId;
    private Long userId;
    private Long carId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualReturnDate;
    private RentalStatus status;
    private BigDecimal totalPrice;
    private BigDecimal latePenalty;
    private LocalDate createdAt;

    // Constructors
    public RentalResponse() {}

    public RentalResponse(Long rentalId, Long userId, Long carId, LocalDate startDate,
                          LocalDate endDate, LocalDate actualReturnDate,
                          RentalStatus status, BigDecimal totalPrice, BigDecimal latePenalty,
                          LocalDate createdAt) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.actualReturnDate = actualReturnDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.latePenalty = latePenalty;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getRentalId() { return rentalId; }
    public void setRentalId(Long rentalId) { this.rentalId = rentalId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDate actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public RentalStatus getStatus() { return status; }
    public void setStatus(RentalStatus status) { this.status = status; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public BigDecimal getLatePenalty() { return latePenalty; }
    public void setLatePenalty(BigDecimal latePenalty) { this.latePenalty = latePenalty; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}