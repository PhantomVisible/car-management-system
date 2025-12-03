package com.carmanagement.rental.domain.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private LocalDate actualReturnDate;  // When actually returned

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status = RentalStatus.PENDING;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "daily_price_at_rental", precision = 10, scale = 2)
    private BigDecimal dailyPriceAtRental;

    @Column(name = "late_penalty", precision = 10, scale = 2)
    private BigDecimal latePenalty;

    private LocalDate createdAt;

    // Constructors
    public Rental() {}

    public Rental(Long userId, Long carId, LocalDate startDate,
                  LocalDate endDate, BigDecimal totalPrice, BigDecimal dailyPriceAtRental) {
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.dailyPriceAtRental = dailyPriceAtRental;
        this.status = RentalStatus.PENDING;
        this.createdAt = LocalDate.now();
    }

    public Rental(Long userId, Long carId, LocalDate startDate,
                  LocalDate endDate, BigDecimal totalPrice) {
        this(userId, carId, startDate, endDate, totalPrice, null);
    }

    // Business logic methods
    public boolean isActive() {
        return status == RentalStatus.ACTIVE;
    }

    public boolean isOverdue() {
        if (actualReturnDate == null) {
            return LocalDate.now().isAfter(endDate);
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
    public BigDecimal getDailyPriceAtRental() { return dailyPriceAtRental; }
    public void setDailyPriceAtRental(BigDecimal dailyPriceAtRental) { this.dailyPriceAtRental = dailyPriceAtRental; }
    public BigDecimal getLatePenalty() { return latePenalty; }
    public void setLatePenalty(BigDecimal latePenalty) { this.latePenalty = latePenalty; }
    public LocalDate getCreatedAt() { return createdAt; }
}