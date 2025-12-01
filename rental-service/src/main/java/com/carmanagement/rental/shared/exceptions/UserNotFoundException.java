package com.carmanagement.rental.shared.exceptions;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException(Long userId) {
        super("User with ID " + userId + " not found", "RENTAL_001");
    }

    public UserNotFoundException(String email) {
        super("User with email " + email + " not found", "RENTAL_002");
    }
}