package com.cargasafe.trip.interfaces.rest.resources;

public record CreateOriginPointResource(String name, String address, Double latitude, Double longitude) {
}
