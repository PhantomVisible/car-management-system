package com.carmanagement.rental.infrastructure.clients;

import com.carmanagement.rental.shared.exceptions.ServiceUnavailableException;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceFallback implements AuthServiceClient {

    @Override
    public UserResponse getUserById(Long userId, String token) {
        // In a real system, we might:
        // 1. Return cached user data
        // 2. Check local database for recent validations
        // 3. For now, just throw exception

        throw new ServiceUnavailableException("Auth", "getUserById");
    }
}