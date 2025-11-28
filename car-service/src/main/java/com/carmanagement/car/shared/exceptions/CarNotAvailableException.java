package com.carmanagement.car.shared.exceptions;

public class CarNotAvailableException extends GlobalException {
    public CarNotAvailableException(Long carId) {
        super("Car with ID " + carId + " is not available for rental", "CAR_002");
    }
}