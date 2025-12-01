package com.carmanagement.rental.infrastructure.clients;

import com.carmanagement.rental.shared.exceptions.ServiceUnavailableException;
import com.carmanagement.rental.shared.exceptions.UserNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        // Handle specific status codes
        if (response.status() == HttpStatus.NOT_FOUND.value()) {
            if (methodKey.contains("AuthServiceClient")) {
                // Extract user ID from method key or response
                return new UserNotFoundException("User not found (Auth Service returned 404)");
            }
            if (methodKey.contains("CarServiceClient")) {
                return new RuntimeException("Car not found");
            }
        }

        if (response.status() == HttpStatus.SERVICE_UNAVAILABLE.value() ||
                response.status() == HttpStatus.GATEWAY_TIMEOUT.value() ||
                response.status() == 0) {  // 0 means connection failed

            if (methodKey.contains("AuthServiceClient")) {
                return new ServiceUnavailableException("Auth");
            }
            if (methodKey.contains("CarServiceClient")) {
                return new ServiceUnavailableException("Car");
            }
            return new ServiceUnavailableException("External");
        }

        // Default handling
        return defaultErrorDecoder.decode(methodKey, response);
    }
}