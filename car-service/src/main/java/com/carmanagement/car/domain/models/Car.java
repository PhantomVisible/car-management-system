package com.carmanagement.car.domain.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer year;

    private String owner;

    @Column(nullable = false)
    private Boolean available = true;

    private BigDecimal dailyPrice;

    private Integer usageCount = 0;

    private LocalDate createdAt;

    // Constructors
    public Car() {}

    public Car(String brand, String model, Integer year, String owner, BigDecimal dailyPrice) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.owner = owner;
        this.dailyPrice = dailyPrice;
        this.available = true;
        this.createdAt = LocalDate.now();
        this.usageCount = 0;
    }

    // Getters and Setters
    public Long getCarId() { return carId; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
    public BigDecimal getDailyPrice() { return dailyPrice; }
    public void setDailyPrice(BigDecimal dailyPrice) { this.dailyPrice = dailyPrice; }
    public Integer getUsageCount() { return usageCount; }
    public void setUsageCount(Integer usageCount) { this.usageCount = usageCount; }
    public LocalDate getCreatedAt() { return createdAt; }
}