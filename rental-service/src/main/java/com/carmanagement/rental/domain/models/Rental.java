package com.carmanagement.rental.domain.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    @Column(nullable = false)
    private Long userId;          // From Auth Service

    @Column(nullable = false)
    private Long carId;           // From Car Service

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    private LocalDateTime actualReturnDate;  // When actually returned

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status = RentalStatus.PENDING;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "daily_price_at_rental", precision = 10, scale = 2)
    private BigDecimal dailyPriceAtRental;

    @Column(name = "late_penalty", precision = 10, scale = 2)
    private BigDecimal latePenalty;

    private LocalDateTime createdAt;

    // Constructors
    public Rental() {}

    public Rental(Long userId, Long carId, LocalDateTime startDate,
                  LocalDateTime endDate, BigDecimal totalPrice, BigDecimal dailyPriceAtRental) {
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.dailyPriceAtRental = dailyPriceAtRental;
        this.status = RentalStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Rental(Long userId, Long carId, LocalDateTime startDate,
                  LocalDateTime endDate, BigDecimal totalPrice) {
        this(userId, carId, startDate, endDate, totalPrice, null);
    }

    // Business logic methods
    public boolean isActive() {
        return status == RentalStatus.ACTIVE;
    }

    public boolean isOverdue() {
        if (actualReturnDate == null) {
            return LocalDateTime.now().isAfter(endDate);
        }
        return actualReturnDate.isAfter(endDate);
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
    public BigDecimal getDailyPriceAtRental() { return dailyPriceAtRental; }
    public void setDailyPriceAtRental(BigDecimal dailyPriceAtRental) { this.dailyPriceAtRental = dailyPriceAtRental; }
    public BigDecimal getLatePenalty() { return latePenalty; }
    public void setLatePenalty(BigDecimal latePenalty) { this.latePenalty = latePenalty; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}