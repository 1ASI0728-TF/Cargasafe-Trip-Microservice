package com.cargasafe.trip.interfaces.rest.transformers;

import com.cargasafe.trip.domain.model.aggregates.Trip;
import com.cargasafe.trip.interfaces.rest.resources.TripResource;

public final class TripResourceFromEntityAssembler {

    public static TripResource toResourceFromEntity(Trip trip) {
        return new TripResource(
                trip.getId(),
                trip.getMerchantId(),
                trip.getDriverId(),
                trip.getDeviceId(),
                trip.getVehicleId(),
                trip.getStatus(),
                trip.getCreatedAt(),
                trip.getStartedAt(),
                trip.getCompletedAt(),
                OriginPointResourceFromEntityAssembler.toResourceFromEntity(trip.getOriginPoint()),
                trip.getDeliveryOrderList().stream()
                        .map(DeliveryOrderResourceFromEntityAssembler::toResourceFromEntity)
                        .toList()
        );
    }
}
