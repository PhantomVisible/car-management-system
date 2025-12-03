package com.carmanagement.car.domain.services;

import com.carmanagement.car.domain.models.Car;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CarScoringDomainService {

    public double calculateCarScore(Car car, double usageWeight, double recencyWeight, double priceWeight) {
        double usageScore = calculateUsageScore(car.getUsageCount());
        double recencyScore = calculateRecencyScore(car.getCreatedAt());
        double priceScore = calculatePriceScore(car.getDailyPrice());

        return (usageScore * usageWeight) +
                (recencyScore * recencyWeight) +
                (priceScore * priceWeight);
    }

    private double calculateUsageScore(Integer usageCount) {
        return Math.min(usageCount / 10.0, 1.0);
    }

    private double calculateRecencyScore(LocalDate createdAt) {
        long daysOld = ChronoUnit.DAYS.between(createdAt, LocalDate.now());
        return Math.max(1.0 - (daysOld / 365.0), 0.1);
    }

    private double calculatePriceScore(java.math.BigDecimal dailyPrice) {
        double price = dailyPrice.doubleValue();
        if (price <= 30) return 1.0;
        if (price <= 60) return 0.8;
        if (price <= 100) return 0.6;
        if (price <= 150) return 0.4;
        return 0.2;
    }

    public String getScoreCategory(double score) {
        if (score >= 0.8) return "EXCELLENT";
        if (score >= 0.6) return "GOOD";
        if (score >= 0.4) return "AVERAGE";
        return "BASIC";
    }
}
