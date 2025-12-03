package com.carmanagement.car.domain.services;

import com.carmanagement.car.domain.models.Car;
import com.carmanagement.car.shared.dtos.CarStatistics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarStatisticsDomainService {

    public CarStatistics calculateStatistics(List<Car> allCars) {
        if (allCars.isEmpty()) {
            return new CarStatistics(); // Return empty stats
        }

        long totalCars = allCars.size();
        long availableCars = allCars.stream().filter(Car::getAvailable).count();

        List<BigDecimal> prices = allCars.stream()
                .map(Car::getDailyPrice)
                .toList();

        BigDecimal avgPrice = calculateAveragePrice(prices);
        BigDecimal minPrice = calculateMinPrice(prices);
        BigDecimal maxPrice = calculateMaxPrice(prices);

        double avgUsage = allCars.stream()
                .mapToInt(Car::getUsageCount)
                .average()
                .orElse(0.0);

        Map<String, Long> brandDistribution = allCars.stream()
                .collect(Collectors.groupingBy(Car::getBrand, Collectors.counting()));

        Car mostPopularCar = allCars.stream()
                .max((c1, c2) -> Integer.compare(c1.getUsageCount(), c2.getUsageCount()))
                .orElse(null);

        return new CarStatistics(totalCars, availableCars, avgPrice, minPrice,
                maxPrice, avgUsage, brandDistribution, mostPopularCar);
    }

    private BigDecimal calculateAveragePrice(List<BigDecimal> prices) {
        BigDecimal sum = prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(prices.size()), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateMinPrice(List<BigDecimal> prices) {
        return prices.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateMaxPrice(List<BigDecimal> prices) {
        return prices.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }
}
