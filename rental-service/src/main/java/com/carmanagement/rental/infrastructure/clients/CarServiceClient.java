package com.carmanagement.rental.infrastructure.clients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(
        name = "car-service",
        url = "http://localhost:8082",
        fallback = CarServiceFallback.class
)
@CircuitBreaker(name = "carService")  // Add circuit breaker
@Retry(name = "carService")           // Add retry mechanism
public interface CarServiceClient {

    @GetMapping("/api/cars/{carId}")
    CarResponse getCarById(
            @PathVariable("carId") Long carId,
            @RequestHeader("Authorization") String token
    );

    @PutMapping("/api/cars/{carId}/rent")
    CarResponse markAsRented(
            @PathVariable("carId") Long carId,
            @RequestHeader("Authorization") String token
    );

    @PutMapping("/api/cars/{carId}/return")
    CarResponse markAsAvailable(
            @PathVariable("carId") Long carId,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/api/cars/{carId}/availability")
    AvailabilityResponse checkAvailability(
            @PathVariable("carId") Long carId,
            @RequestHeader("Authorization") String token
    );

    // Inner classes for responses
    class CarResponse {
        private Long carId;
        private String brand;
        private String model;
        private BigDecimal dailyPrice;
        private boolean available;

        // Getters and setters
        public Long getCarId() { return carId; }
        public void setCarId(Long carId) { this.carId = carId; }
        public String getBrand() { return brand; }
        public void setBrand(String brand) { this.brand = brand; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public BigDecimal getDailyPrice() { return dailyPrice; }
        public void setDailyPrice(BigDecimal dailyPrice) { this.dailyPrice = dailyPrice; }
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
    }

    class AvailabilityResponse {
        private boolean available;
        private String message;

        // Getters and setters
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}