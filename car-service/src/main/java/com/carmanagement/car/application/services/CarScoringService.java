package com.carmanagement.car.application.services;

import com.carmanagement.car.domain.models.Car;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class CarScoringService {

    @Value("${car.scoring.weights.usage:0.4}")
    private double usageWeight;

    @Value("${car.scoring.weights.recency:0.3}")
    private double recencyWeight;

    @Value("${car.scoring.weights.price:0.3}")
    private double priceWeight;

    @Value("${car.scoring.price.thresholds.low:30}")
    private double lowPriceThreshold;

    @Value("${car.scoring.price.thresholds.medium:60}")
    private double mediumPriceThreshold;

    @Value("${car.scoring.price.thresholds.high:100}")
    private double highPriceThreshold;

    public double calculateCarScore(Car car) {
        validateWeights();

        double usageScore = calculateUsageScore(car.getUsageCount());
        double recencyScore = calculateRecencyScore(car.getCreatedAt());
        double priceScore = calculatePriceScore(car.getDailyPrice());

        return (usageScore * usageWeight) +
                (recencyScore * recencyWeight) +
                (priceScore * priceWeight);
    }

    private void validateWeights() {
        double total = usageWeight + recencyWeight + priceWeight;
        if (Math.abs(total - 1.0) > 0.01) {
            throw new IllegalStateException(
                    "Scoring weights must sum to 1.0. Current sum: " + total);
        }
    }

    private double calculateUsageScore(Integer usageCount) {
        // More usage = higher score (proven reliability)
        return Math.min(usageCount / 10.0, 1.0); // Normalize to 0-1
    }

    private double calculateRecencyScore(LocalDateTime createdAt) {
        // Newer cars get higher scores
        long daysOld = ChronoUnit.DAYS.between(createdAt, LocalDateTime.now());
        return Math.max(1.0 - (daysOld / 365.0), 0.1); // 0.1 to 1.0 range
    }

    private double calculatePriceScore(java.math.BigDecimal dailyPrice) {
        // Lower price = higher score (better value)
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

    public double getUsageWeight() { return usageWeight; }
    public double getRecencyWeight() { return recencyWeight; }
    public double getPriceWeight() { return priceWeight; }
}