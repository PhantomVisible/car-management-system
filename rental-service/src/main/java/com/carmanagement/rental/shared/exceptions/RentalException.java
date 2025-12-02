package com.carmanagement.rental.shared.exceptions;

public class RentalException extends RuntimeException {
    public RentalException(String message) {
        super(message);
    }

    public RentalException(String message, Throwable cause) {
        super(message, cause);
    }
}