package com.cargasafe.trip.domain.exceptions;

public class OriginPointNotFoundException extends RuntimeException {
    public OriginPointNotFoundException(Long id) {
        super("OriginPoint not found with id: " + id);
    }
}
