package com.cargasafe.trip.domain.services;

import com.cargasafe.trip.domain.model.entities.DeliveryOrder;
import com.cargasafe.trip.domain.model.queries.DeliveryOrderExistsQuery;
import com.cargasafe.trip.domain.model.queries.GetAllDeliveryOrdersQuery;

import java.util.List;

public interface DeliveryOrderQueryService {
    List<DeliveryOrder> handle(GetAllDeliveryOrdersQuery query);
    boolean handle(DeliveryOrderExistsQuery query);
}
