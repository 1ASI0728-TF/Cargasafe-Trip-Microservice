package com.cargasafe.trip.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateTripDeliveryOrderResource(
        @Email String clientEmail,
        @NotBlank String address,
        @NotNull Double latitude,
        @NotNull Double longitude,
        @NotNull @Positive Long sequenceOrder,
        Double maxHumidity, Double minHumidity,
        Double maxTemperature, Double minTemperature, Double maxVibration) {
}
