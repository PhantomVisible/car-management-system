package com.carmanagement.car.infrastructure.repositories;

import com.carmanagement.car.domain.models.Car;
import com.carmanagement.car.domain.ports.CarRepositoryPort;
import com.carmanagement.car.domain.ports.CarSearchPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepositoryAdapter extends JpaRepository<Car, Long>,
                                                CarRepositoryPort,
                                                CarSearchPort {

    @Override
    List<Car> findByAvailableTrue();

    @Override
    List<Car> findByBrandAndModel(String brand, String model);

    @Override
    List<Car> findByYear(Integer year);

    // Custom query for search/filter functionality
    @Override
    @Query("SELECT c FROM Car c WHERE " +
            "(:brand IS NULL OR c.brand = :brand) AND " +
            "(:model IS NULL OR c.model = :model) AND " +
            "(:minYear IS NULL OR c.year >= :minYear) AND " +
            "(:maxYear IS NULL OR c.year <= :maxYear) AND " +
            "(:available IS NULL OR c.available = :available)")
    List<Car> searchCars(@Param("brand") String brand,
                         @Param("model") String model,
                         @Param("minYear") Integer minYear,
                         @Param("maxYear") Integer maxYear,
                         @Param("available") Boolean available);
}