package com.carmanagement.rental.infrastructure.controllers;

import com.carmanagement.rental.infrastructure.clients.AuthServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class CircuitTestController {

    private final AuthServiceClient authServiceClient;

    public CircuitTestController(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    @GetMapping("/circuit")
    @CircuitBreaker(name = "testCircuit", fallbackMethod = "fallbackMethod")
    public String testCircuit(@RequestHeader("Authorization") String token) {
        // This will call Auth Service
        AuthServiceClient.UserResponse user =
                authServiceClient.getUserById(1L, token);
        return "Success: " + user.getEmail();
    }

    // Fallback method
    public String fallbackMethod(String token, Exception e) {
        return "Fallback: Auth Service unavailable. Error: " + e.getMessage();
    }
}