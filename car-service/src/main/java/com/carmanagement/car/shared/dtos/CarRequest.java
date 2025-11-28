package com.carmanagement.car.shared.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class CarRequest {

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Year is required")
    @Positive(message = "Year must be positive")
    private Integer year;

    @NotBlank(message = "Owner is required")
    private String owner;

    @NotNull(message = "Daily price is required")
    @Positive(message = "Daily price must be positive")
    private BigDecimal dailyPrice;

    // Constructors
    public CarRequest() {}

    public CarRequest(String brand, String model, Integer year, String owner, BigDecimal dailyPrice) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.owner = owner;
        this.dailyPrice = dailyPrice;
    }

    // Getters and Setters
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public BigDecimal getDailyPrice() { return dailyPrice; }
    public void setDailyPrice(BigDecimal dailyPrice) { this.dailyPrice = dailyPrice; }
}