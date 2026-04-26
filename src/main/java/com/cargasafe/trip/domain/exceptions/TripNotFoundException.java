package com.cargasafe.trip.domain.exceptions;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException(Long id) {
        super("Trip not found with id: " + id);
    }
}
