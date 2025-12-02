package com.carmanagement.rental.infrastructure.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("service", applicationName);
        healthInfo.put("port", serverPort);
        healthInfo.put("timestamp", LocalDateTime.now());
        healthInfo.put("status", "UP");

        try {
            // Test database connection
            Integer dbResult = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            healthInfo.put("database", "CONNECTED");
            healthInfo.put("databaseTest", dbResult);

            // Test if rentals table exists (optional)
            try {
                jdbcTemplate.queryForObject("SELECT COUNT(*) FROM rentals", Integer.class);
                healthInfo.put("rentalsTable", "EXISTS");
            } catch (Exception e) {
                healthInfo.put("rentalsTable", "NOT_CREATED_YET");
            }

        } catch (Exception e) {
            healthInfo.put("database", "ERROR");
            healthInfo.put("databaseError", e.getMessage());
            healthInfo.put("status", "DOWN");
            return ResponseEntity.status(503).body(healthInfo);
        }

        return ResponseEntity.ok(healthInfo);
    }

    @GetMapping("/simple")
    public Map<String, String> simpleHealth() {
        return Map.of(
                "status", "UP",
                "service", applicationName,
                "timestamp", LocalDateTime.now().toString()
        );
    }

    @GetMapping("/info")
    public Map<String, Object> serviceInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "Car Rental Service");
        info.put("version", "1.0.0");
        info.put("description", "Microservice for managing car rentals");
        info.put("status", "operational");
        info.put("timestamp", LocalDateTime.now());
        info.put("endpoints", Map.of(
                "createRental", "POST /api/rentals",
                "getMyRentals", "GET /api/rentals/my-rentals",
                "returnCar", "PUT /api/rentals/{id}/return",
                "health", "GET /api/health"
        ));

        return info;
    }
}