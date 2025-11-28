package com.carmanagement.car.domain.ports;

import com.carmanagement.car.domain.models.Car;
import java.util.List;
import java.util.Optional;

public interface CarRepositoryPort {
    Car save(Car car);
    Optional<Car> findById(Long carId);
    List<Car> findAll();
    List<Car> findByAvailableTrue();
    List<Car> findByBrandAndModel(String brand, String model);
    List<Car> findByYear(Integer year);
    void deleteById(Long carId);
    boolean existsById(Long carId);
}