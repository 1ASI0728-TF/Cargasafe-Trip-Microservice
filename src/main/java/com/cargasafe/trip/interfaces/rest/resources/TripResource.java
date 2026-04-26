package com.cargasafe.trip.interfaces.rest.resources;

import com.cargasafe.trip.domain.model.valueobjects.TripStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TripResource(Long id, Long merchantId, Long driverId, Long deviceId, Long vehicleId,
                           TripStatus status, LocalDateTime createdAt, LocalDateTime startedAt,
                           LocalDateTime completedAt, OriginPointResource originPoint,
                           List<DeliveryOrderResource> deliveryOrders) {
}
