package com.carmanagement.car.shared.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CarResponse {
    private Long carId;
    private String brand;
    private String model;
    private Integer year;
    private String owner;
    private Boolean available;
    private BigDecimal dailyPrice;
    private Integer usageCount;
    private LocalDate createdAt;

    private Double score;           // 0.0 to 1.0
    private String scoreCategory;   // EXCELLENT, GOOD, etc.

    // Constructors
    public CarResponse() {}

    public CarResponse(Long carId, String brand, String model, Integer year, String owner,
                       Boolean available, BigDecimal dailyPrice, Integer usageCount, LocalDate createdAt, Double score, String scoreCategory) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.owner = owner;
        this.available = available;
        this.dailyPrice = dailyPrice;
        this.usageCount = usageCount;
        this.createdAt = createdAt;

        this.score = score;
        this.scoreCategory = scoreCategory;
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
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public String getScoreCategory() { return scoreCategory; }
    public void setScoreCategory(String scoreCategory) { this.scoreCategory = scoreCategory; }
}