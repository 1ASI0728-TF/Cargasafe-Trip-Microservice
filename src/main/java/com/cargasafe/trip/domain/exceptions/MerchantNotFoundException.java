package com.cargasafe.trip.domain.exceptions;

public class MerchantNotFoundException extends RuntimeException {
    public MerchantNotFoundException(Long merchantId) {
        super("Merchant not found with id: " + merchantId);
    }
}