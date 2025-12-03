package com.carmanagement.rental.infrastructure.clients;

import com.carmanagement.rental.shared.dtos.UserResponsePath;
import com.carmanagement.rental.shared.exceptions.ServiceUnavailableException;
import com.carmanagement.rental.shared.exceptions.UserNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", url = "http://localhost:8081/api/auth")
@CircuitBreaker(name = "authService")  // Add circuit breaker
@Retry(name = "authService")           // Add retry mechanism
public interface AuthServiceClient {

    @GetMapping("/users/{userId}")
    UserResponsePath getUserById(
            @PathVariable("userId") Long userId,
            @RequestHeader("Authorization") String token
    );

    @Component
    public class AuthServiceFallback implements AuthServiceClient {
        public UserResponsePath getUserById(Long userId, String token) {
            // Return cached user or throw ServiceUnavailableException
            throw new ServiceUnavailableException("Auth service is unavailable");
        }
    }
}