package com.cargasafe.trip.interfaces.rest.resources;

import java.time.LocalDateTime;

public record DeliveryOrderResource(Long id, Long tripId, String clientEmail, Long sequenceOrder,
                                    String address, LocalDateTime arrivalAt, String status,
                                    Double minHumidity, Double maxHumidity,
                                    Double minTemperature, Double maxTemperature, Double maxVibration,
                                    Double latitude, Double longitude, LocalDateTime createdAt) {
}
