package com.cargasafe.trip.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record OrderThresholds(
        Double minHumidity,
        Double maxHumidity,
        Double maxTemperature,
        Double minTemperature,
        Double maxVibration
) {
}
