package com.carmanagement.auth.shared.exceptions;

public class UserAlreadyExistsException extends GlobalException {
    public UserAlreadyExistsException(String email) {
        super("User with email " + email + " already exists", "AUTH_001");
    }
}