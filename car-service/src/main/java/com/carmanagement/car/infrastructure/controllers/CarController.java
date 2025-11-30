package com.carmanagement.car.infrastructure.controllers;

import com.carmanagement.car.application.services.CarService;
import com.carmanagement.car.domain.models.Car;
import com.carmanagement.car.domain.services.CarScoringService;
import com.carmanagement.car.domain.services.CarStatisticsService;
import com.carmanagement.car.shared.dtos.CarRequest;
import com.carmanagement.car.shared.dtos.CarResponse;
import com.carmanagement.car.shared.dtos.CarStatistics;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;
    private final CarScoringService carScoringService;
    private final CarStatisticsService carStatisticsService;

    public CarController(CarService carService, CarScoringService carScoringService, CarStatisticsService carStatisticsService) {
        this.carService = carService;
        this.carScoringService = carScoringService;
        this.carStatisticsService = carStatisticsService;
    }

    @PostMapping
    public ResponseEntity<CarResponse> addCar(@Valid @RequestBody CarRequest carRequest) {
        Car car = new Car(
                carRequest.getBrand(),
                carRequest.getModel(),
                carRequest.getYear(),
                carRequest.getOwner(),
                carRequest.getDailyPrice()
        );

        Car savedCar = carService.addCar(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(toCarResponse(savedCar));
    }

    @GetMapping
    public ResponseEntity<List<CarResponse>> getAllCars() {
        List<CarResponse> cars = carService.getAllCars().stream()
                .map(this::toCarResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/available")
    public ResponseEntity<List<CarResponse>> getAvailableCars() {
        List<CarResponse> cars = carService.getAvailableCars().stream()
                .map(this::toCarResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<CarResponse>> getRecommendedCars() {
        List<CarResponse> cars = carService.getRecommendedCars().stream()
                .map(this::toCarResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CarResponse>> searchCars(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(required = false) Boolean available) {

        List<CarResponse> cars = carService.searchCars(brand, model, minYear, maxYear, available)
                .stream()
                .map(this::toCarResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/statistics")
    public ResponseEntity<CarStatistics> getCarStatistics() {
        CarStatistics statistics = carStatisticsService.calculateStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/{carId}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long carId) {
        Car car = carService.getCarById(carId);
        return ResponseEntity.ok(toCarResponse(car));
    }

    @PutMapping("/{carId}")
    public ResponseEntity<CarResponse> updateCar(
            @PathVariable Long carId,
            @Valid @RequestBody CarRequest carRequest) {

        Car carDetails = new Car(
                carRequest.getBrand(),
                carRequest.getModel(),
                carRequest.getYear(),
                carRequest.getOwner(),
                carRequest.getDailyPrice()
        );

        Car updatedCar = carService.updateCar(carId, carDetails);
        return ResponseEntity.ok(toCarResponse(updatedCar));
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long carId) {
        carService.deleteCar(carId);
        return ResponseEntity.noContent().build();
    }

    // Helper method to convert Car entity to CarResponse DTO
    private CarResponse toCarResponse(Car car) {
        double score = carScoringService.calculateCarScore(car);
        String scoreCategory = carScoringService.getScoreCategory(score);

        return new CarResponse(
                car.getCarId(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getOwner(),
                car.getAvailable(),
                car.getDailyPrice(),
                car.getUsageCount(),
                car.getCreatedAt(),
                score,
                scoreCategory
        );
    }
}