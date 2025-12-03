package com.carmanagement.rental.infrastructure.clients;

import com.carmanagement.rental.shared.dtos.UserResponsePath;
import com.carmanagement.rental.shared.exceptions.ServiceUnavailableException;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceFallback implements AuthServiceClient {

    @Override
    public UserResponsePath getUserById(Long userId, String token) {
        throw new ServiceUnavailableException("Auth service is unavailable");
    }
}
