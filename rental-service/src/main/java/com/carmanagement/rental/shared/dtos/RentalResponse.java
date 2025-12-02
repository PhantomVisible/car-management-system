package com.carmanagement.rental.shared.dtos;

import com.carmanagement.rental.domain.models.RentalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RentalResponse {

    private Long rentalId;
    private Long userId;
    private Long carId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime actualReturnDate;
    private RentalStatus status;
    private BigDecimal totalPrice;
    private BigDecimal latePenalty;
    private LocalDateTime createdAt;

    // Constructors
    public RentalResponse() {}

    public RentalResponse(Long rentalId, Long userId, Long carId, LocalDateTime startDate,
                          LocalDateTime endDate, LocalDateTime actualReturnDate,
                          RentalStatus status, BigDecimal totalPrice, BigDecimal latePenalty,
                          LocalDateTime createdAt) {
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

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public LocalDateTime getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDateTime actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public RentalStatus getStatus() { return status; }
    public void setStatus(RentalStatus status) { this.status = status; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public BigDecimal getLatePenalty() { return latePenalty; }
    public void setLatePenalty(BigDecimal latePenalty) { this.latePenalty = latePenalty; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}