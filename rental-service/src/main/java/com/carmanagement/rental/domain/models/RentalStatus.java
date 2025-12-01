package com.carmanagement.rental.domain.models;

public enum RentalStatus {
    PENDING,     // Created but payment pending
    CONFIRMED,   // Payment received, waiting for pickup
    ACTIVE,      // Currently rented (car is with customer)
    COMPLETED,   // Successfully returned on time
    CANCELLED,   // Cancelled before becoming ACTIVE
    OVERDUE      // Not returned by due date
}