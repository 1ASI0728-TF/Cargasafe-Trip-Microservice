package com.cargasafe.trip.interfaces.rest.transformers;

import com.cargasafe.trip.domain.model.entities.DeliveryOrder;
import com.cargasafe.trip.domain.model.valueobjects.OrderThresholds;
import com.cargasafe.trip.interfaces.rest.resources.DeliveryOrderResource;

import java.util.Optional;

public final class DeliveryOrderResourceFromEntityAssembler {

    public static DeliveryOrderResource toResourceFromEntity(DeliveryOrder entity) {
        var thresholds = entity.getOrderThresholds();
        var location = entity.getLocation();

        return new DeliveryOrderResource(
                entity.getId(),
                entity.getTrip().getId(),
                entity.getClientEmail(),
                entity.getSequenceOrder(),
                location.address(),
                entity.getArrivalAt(),
                entity.getStatus().name(),
                Optional.ofNullable(thresholds).map(OrderThresholds::minHumidity).orElse(null),
                Optional.ofNullable(thresholds).map(OrderThresholds::maxHumidity).orElse(null),
                Optional.ofNullable(thresholds).map(OrderThresholds::minTemperature).orElse(null),
                Optional.ofNullable(thresholds).map(OrderThresholds::maxTemperature).orElse(null),
                Optional.ofNullable(thresholds).map(OrderThresholds::maxVibration).orElse(null),
                location.latitude(),
                location.longitude(),
                entity.getCreatedAt()
        );
    }
}
