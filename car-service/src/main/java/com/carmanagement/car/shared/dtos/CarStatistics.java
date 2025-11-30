package com.carmanagement.car.shared.dtos;

import com.carmanagement.car.domain.models.Car;

import java.math.BigDecimal;
import java.util.Map;

public class CarStatistics {
    private Long totalCars;
    private Long availableCars;
    private BigDecimal averageDailyPrice;
    private BigDecimal minDailyPrice;
    private BigDecimal maxDailyPrice;
    private Double averageUsageCount;
    private Map<String, Long> brandDistribution;
    private Car mostPopularCar;

    // Constructors
    public CarStatistics() {
        // Default constructor for empty case
    }

    public CarStatistics(Long totalCars, Long availableCars, BigDecimal averageDailyPrice,
                         BigDecimal minDailyPrice, BigDecimal maxDailyPrice, Double averageUsageCount,
                         Map<String, Long> brandDistribution, Car mostPopularCar) {
        this.totalCars = totalCars;
        this.availableCars = availableCars;
        this.averageDailyPrice = averageDailyPrice;
        this.minDailyPrice = minDailyPrice;
        this.maxDailyPrice = maxDailyPrice;
        this.averageUsageCount = averageUsageCount;
        this.brandDistribution = brandDistribution;
        this.mostPopularCar = mostPopularCar;
    }

    // Getters and Setters
    public Long getTotalCars() { return totalCars; }
    public void setTotalCars(Long totalCars) { this.totalCars = totalCars; }
    public Long getAvailableCars() { return availableCars; }
    public void setAvailableCars(Long availableCars) { this.availableCars = availableCars; }
    public BigDecimal getAverageDailyPrice() { return averageDailyPrice; }
    public void setAverageDailyPrice(BigDecimal averageDailyPrice) { this.averageDailyPrice = averageDailyPrice; }
    public BigDecimal getMinDailyPrice() { return minDailyPrice; }
    public void setMinDailyPrice(BigDecimal minDailyPrice) { this.minDailyPrice = minDailyPrice; }
    public BigDecimal getMaxDailyPrice() { return maxDailyPrice; }
    public void setMaxDailyPrice(BigDecimal maxDailyPrice) { this.maxDailyPrice = maxDailyPrice; }
    public Double getAverageUsageCount() { return averageUsageCount; }
    public void setAverageUsageCount(Double averageUsageCount) { this.averageUsageCount = averageUsageCount; }
    public Map<String, Long> getBrandDistribution() { return brandDistribution; }
    public void setBrandDistribution(Map<String, Long> brandDistribution) { this.brandDistribution = brandDistribution; }
    public Car getMostPopularCar() { return mostPopularCar; }
    public void setMostPopularCar(Car mostPopularCar) { this.mostPopularCar = mostPopularCar; }

    // Helper methods
    public Double getUtilizationRate() {
        if (totalCars == null || totalCars == 0) return 0.0;
        return (totalCars - availableCars) / (double) totalCars * 100;
    }
}