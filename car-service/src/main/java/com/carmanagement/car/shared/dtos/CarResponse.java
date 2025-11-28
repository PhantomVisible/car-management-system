package com.carmanagement.car.shared.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CarResponse {
    private Long carId;
    private String brand;
    private String model;
    private Integer year;
    private String owner;
    private Boolean available;
    private BigDecimal dailyPrice;
    private Integer usageCount;
    private LocalDateTime createdAt;

    // Constructors
    public CarResponse() {}

    public CarResponse(Long carId, String brand, String model, Integer year, String owner,
                       Boolean available, BigDecimal dailyPrice, Integer usageCount, LocalDateTime createdAt) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.owner = owner;
        this.available = available;
        this.dailyPrice = dailyPrice;
        this.usageCount = usageCount;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}