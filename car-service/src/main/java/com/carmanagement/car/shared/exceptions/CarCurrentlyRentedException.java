package com.carmanagement.car.shared.exceptions;

public class CarCurrentlyRentedException extends GlobalException {
    public CarCurrentlyRentedException(Long carId) {
        super("Car with ID " + carId + " is currently rented and cannot be deleted",
                "CAR_003");
    }
}