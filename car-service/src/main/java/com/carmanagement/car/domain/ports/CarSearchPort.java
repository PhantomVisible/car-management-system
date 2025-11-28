package com.carmanagement.car.domain.ports;

import com.carmanagement.car.domain.models.Car;
import java.util.List;

public interface CarSearchPort {
    List<Car> searchCars(String brand, String model, Integer minYear,
                         Integer maxYear, Boolean available);
}