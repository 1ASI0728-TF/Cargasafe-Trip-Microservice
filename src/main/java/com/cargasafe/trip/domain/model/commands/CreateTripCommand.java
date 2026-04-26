package com.cargasafe.trip.domain.model.commands;

import com.cargasafe.trip.domain.model.entities.DeliveryOrder;

import java.util.List;

public record CreateTripCommand(Long driverId, Long deviceId, Long vehicleId, Long merchantId,
                                List<DeliveryOrder> deliveryOrderList, Long originPointId) {
}
