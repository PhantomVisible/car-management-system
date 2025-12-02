package com.carmanagement.rental.shared.exceptions;

public class RentalNotFoundException extends RentalException {
    public RentalNotFoundException(Long rentalId) {
        super("Rental with ID " + rentalId + " not found");
    }
}