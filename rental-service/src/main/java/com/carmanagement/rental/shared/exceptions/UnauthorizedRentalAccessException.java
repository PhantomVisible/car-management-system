package com.carmanagement.rental.shared.exceptions;

public class UnauthorizedRentalAccessException extends RentalException {
    public UnauthorizedRentalAccessException(Long userId, Long rentalId) {
        super("User " + userId + " is not authorized to access rental " + rentalId);
    }
}