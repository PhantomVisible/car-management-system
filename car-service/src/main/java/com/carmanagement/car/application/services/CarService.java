package com.carmanagement.car.application.services;

import com.carmanagement.car.domain.ports.CarSearchPort;
import com.carmanagement.car.domain.models.Car;
import com.carmanagement.car.domain.ports.CarRepositoryPort;
import com.carmanagement.car.shared.exceptions.CarCurrentlyRentedException;
import com.carmanagement.car.shared.exceptions.CarNotFoundException;
import com.carmanagement.car.shared.exceptions.CarNotAvailableException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {
    private final CarRepositoryPort carRepository;
    private final CarSearchPort carSearchPort;
    private final CarScoringService carScoringService;

    public CarService(CarRepositoryPort carRepository, CarSearchPort carSearchPort, CarScoringService carScoringService) {
        this.carRepository = carRepository;
        this.carSearchPort = carSearchPort;
        this.carScoringService = carScoringService;
    }

    public List<Car> getRecommendedCars() {
        List<Car> availableCars = carRepository.findByAvailableTrue();

        // Score and sort cars
        return availableCars.stream()
                .sorted((c1, c2) -> Double.compare(
                        carScoringService.calculateCarScore(c2), // Descending order
                        carScoringService.calculateCarScore(c1)
                ))
                .collect(Collectors.toList());
    }

    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    public Car updateCar(Long carId, Car carDetails) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));

        // Update fields if provided
        Optional.ofNullable(carDetails.getBrand()).ifPresent(car::setBrand);
        Optional.ofNullable(carDetails.getModel()).ifPresent(car::setModel);
        Optional.ofNullable(carDetails.getYear()).ifPresent(car::setYear);
        Optional.ofNullable(carDetails.getOwner()).ifPresent(car::setOwner);
        Optional.ofNullable(carDetails.getDailyPrice()).ifPresent(car::setDailyPrice);
        Optional.ofNullable(carDetails.getAvailable()).ifPresent(car::setAvailable);

        return carRepository.save(car);
    }

    public void deleteCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));
        if (!car.getAvailable()) {
            throw new CarCurrentlyRentedException(carId);
        }
        carRepository.deleteById(carId);
    }

    public Car getCarById(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getAvailableCars() {
        return carRepository.findByAvailableTrue();
    }

    public List<Car> searchCars(String brand, String model, Integer minYear, Integer maxYear, Boolean available) {
        return carSearchPort.searchCars(brand, model, minYear, maxYear, available);
    }

    public Car markAsRented(Long carId) {
        Car car = getCarById(carId);
        if (!car.getAvailable()) {
            throw new CarNotAvailableException(carId);
        }
        car.setAvailable(false);
        car.setUsageCount(car.getUsageCount() + 1);
        return carRepository.save(car);
    }

    public Car markCarAsAvailable(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));
        car.setAvailable(true);
        return carRepository.save(car);
    }
}