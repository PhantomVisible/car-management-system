package com.carmanagement.auth.shared.exceptions;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException(String email) {
        super("User with email " + email + " not found", "AUTH_002");
    }

    public UserNotFoundException(Long userId) {
        super("User with ID " + userId + " not found", "AUTH_003");
    }
}