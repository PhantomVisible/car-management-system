package com.carmanagement.car.application.services;

import com.carmanagement.car.domain.models.Car;
import com.carmanagement.car.domain.services.CarScoringDomainService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CarScoringService {

    private final CarScoringDomainService domainService = new CarScoringDomainService();

    @Value("${car.scoring.weights.usage:0.4}")
    private double usageWeight;

    @Value("${car.scoring.weights.recency:0.3}")
    private double recencyWeight;

    @Value("${car.scoring.weights.price:0.3}")
    private double priceWeight;

    public double calculateCarScore(Car car) {
        return domainService.calculateCarScore(car, usageWeight, recencyWeight, priceWeight);
    }

    public String getScoreCategory(double score) {
        return domainService.getScoreCategory(score);
    }
}
