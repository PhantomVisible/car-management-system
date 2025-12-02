package com.carmanagement.rental.infrastructure.clients;

import com.carmanagement.rental.shared.exceptions.ServiceUnavailableException;
import com.carmanagement.rental.shared.exceptions.UserNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "auth-service",
        url = "http://localhost:8081",
        fallback = AuthServiceFallback.class
)
@CircuitBreaker(name = "authService")  // Add circuit breaker
@Retry(name = "authService")           // Add retry mechanism
public interface AuthServiceClient {

    @GetMapping("/api/auth/users/{userId}")
    UserResponse getUserById(
            @PathVariable("userId") Long userId,
            @RequestHeader("Authorization") String token
    );

    @Component
    public class AuthServiceFallback implements AuthServiceClient {
        public UserResponse getUserById(Long userId, String token) {
            // Return cached user or throw ServiceUnavailableException
            throw new ServiceUnavailableException("Auth service is unavailable");
        }
    }
    // Inner class for response
    class UserResponse {
        private Long userId;
        private String email;
        private String role;
        private boolean active;

        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }
}