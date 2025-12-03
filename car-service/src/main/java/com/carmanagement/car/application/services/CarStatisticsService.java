package com.carmanagement.car.application.services;

import com.carmanagement.car.domain.models.Car;
import com.carmanagement.car.domain.ports.CarRepositoryPort;
import com.carmanagement.car.domain.services.CarStatisticsDomainService;
import com.carmanagement.car.shared.dtos.CarStatistics;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarStatisticsService {

    private final CarRepositoryPort carRepository;
    private final CarStatisticsDomainService domainService = new CarStatisticsDomainService();

    public CarStatisticsService(CarRepositoryPort carRepository) {
        this.carRepository = carRepository;
    }

    public CarStatistics calculateStatistics() {
        List<Car> allCars = carRepository.findAll();
        return domainService.calculateStatistics(allCars);
    }
}
