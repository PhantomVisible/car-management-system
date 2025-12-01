package com.carmanagement.rental.shared.exceptions;

import java.time.LocalDateTime;

public class GlobalException extends RuntimeException {
    private final LocalDateTime timestamp;
    private final String errorCode;

    public GlobalException(String message, String errorCode) {
        super(message);
        this.timestamp = LocalDateTime.now();
        this.errorCode = errorCode;
    }

    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getErrorCode() { return errorCode; }
}