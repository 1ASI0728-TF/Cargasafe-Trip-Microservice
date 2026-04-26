package com.cargasafe.trip.domain.services;

import com.cargasafe.trip.domain.model.commands.DeliverDeliveryOrderCommand;

public interface DeliveryOrderCommandService {
    void handle(DeliverDeliveryOrderCommand command);
}
