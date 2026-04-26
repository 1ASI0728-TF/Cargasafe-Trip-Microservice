package com.cargasafe.trip.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Location(String address, Double latitude, Double longitude) {
}
