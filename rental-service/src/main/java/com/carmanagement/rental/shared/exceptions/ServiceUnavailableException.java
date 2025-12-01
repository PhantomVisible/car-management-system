package com.carmanagement.rental.shared.exceptions;

public class ServiceUnavailableException extends GlobalException {
    public ServiceUnavailableException(String serviceName) {
        super(serviceName + " service is currently unavailable",
                "SERVICE_001");
    }

    public ServiceUnavailableException(String serviceName, String operation) {
        super(serviceName + " service unavailable for operation: " + operation,
                "SERVICE_002");
    }
}