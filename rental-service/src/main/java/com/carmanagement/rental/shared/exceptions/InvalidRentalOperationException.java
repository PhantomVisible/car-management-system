package com.carmanagement.rental.shared.exceptions;

public class InvalidRentalOperationException extends RentalException {
    public InvalidRentalOperationException(String operation, String reason) {
        super("Cannot " + operation + ": " + reason);
    }
}