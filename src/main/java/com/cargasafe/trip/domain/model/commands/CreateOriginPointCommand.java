package com.cargasafe.trip.domain.model.commands;

public record CreateOriginPointCommand(String name, String address, Double latitude, Double longitude) {
}
