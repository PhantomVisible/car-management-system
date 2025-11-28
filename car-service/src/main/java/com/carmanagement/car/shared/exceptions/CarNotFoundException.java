package com.carmanagement.car.shared.exceptions;

public class CarNotFoundException extends GlobalException {
    public CarNotFoundException(Long carId) {
        super("Car with ID " + carId + " not found", "CAR_001");
    }
}