package com.cargasafe.trip.domain.exceptions;

public class DeliveryOrderNotFoundException extends RuntimeException {
    public DeliveryOrderNotFoundException(Long id) {
        super("DeliveryOrder not found with id: " + id);
    }
}
