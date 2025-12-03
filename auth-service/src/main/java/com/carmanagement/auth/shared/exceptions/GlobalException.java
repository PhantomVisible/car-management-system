package com.carmanagement.auth.shared.exceptions;

import java.time.LocalDate;

public class GlobalException extends RuntimeException {
    private final LocalDate timestamp;
    private final String errorCode;

    public GlobalException(String message, String errorCode) {
        super(message);
        this.timestamp = LocalDate.now();
        this.errorCode = errorCode;
    }

    // Getters
    public LocalDate getTimestamp() { return timestamp; }
    public String getErrorCode() { return errorCode; }
}