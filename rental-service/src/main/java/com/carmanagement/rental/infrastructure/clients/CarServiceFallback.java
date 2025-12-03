package com.carmanagement.rental.infrastructure.clients;

import com.carmanagement.rental.shared.exceptions.ServiceUnavailableException;
import org.springframework.stereotype.Component;

@Component
public class CarServiceFallback implements CarServiceClient {

    @Override
    public CarResponse getCarById(Long carId, String token) {
        throw new ServiceUnavailableException("Car Service unavailable for getCarById");
    }

    @Override
    public CarResponse markAsRented(Long carId, String token) {
        throw new ServiceUnavailableException("Car Service unavailable for markAsRented");
    }

    @Override
    public CarResponse markAsAvailable(Long carId, String token) {
        throw new ServiceUnavailableException("Car Service unavailable for markAsAvailable");
    }

    @Override
    public CarResponse markAsAvailableAfterCancel(Long carId, String token) {
        throw new ServiceUnavailableException("Car Service unavailable for markAsAvailableAfterCancel");
    }

    @Override
    public AvailabilityResponse checkAvailability(Long carId, String token) {
        throw new ServiceUnavailableException("Car Service unavailable for checkAvailability");
    }
}
