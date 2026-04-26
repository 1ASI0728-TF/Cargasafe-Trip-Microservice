package com.cargasafe.trip.interfaces.rest.transformers;

import com.cargasafe.trip.domain.model.commands.CreateTripCommand;
import com.cargasafe.trip.domain.model.entities.DeliveryOrder;
import com.cargasafe.trip.domain.model.valueobjects.Location;
import com.cargasafe.trip.domain.model.valueobjects.OrderThresholds;
import com.cargasafe.trip.interfaces.rest.resources.CreateTripResource;

public final class CreateTripCommandFromResourceAssembler {

    public static CreateTripCommand toCommandFromResource(CreateTripResource resource) {
        var deliveryOrders = resource.deliveryOrders().stream().map(o -> {
            var entity = new DeliveryOrder();
            entity.setSequenceOrder(o.sequenceOrder());
            entity.setClientEmail(o.clientEmail());
            entity.setOrderThresholds(new OrderThresholds(o.minHumidity(), o.maxHumidity(), o.maxTemperature(), o.minTemperature(), o.maxVibration()));
            entity.setLocation(new Location(o.address(), o.latitude(), o.longitude()));
            return entity;
        }).toList();

        return new CreateTripCommand(resource.driverId(), resource.deviceId(), resource.vehicleId(),
                resource.merchantId(), deliveryOrders, resource.originPointId());
    }
}
