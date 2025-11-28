package com.carmanagement.auth.shared.exceptions;

public class InvalidCredentialsException extends GlobalException {
    public InvalidCredentialsException() {
        super("Invalid email or password", "AUTH_004");
    }
}