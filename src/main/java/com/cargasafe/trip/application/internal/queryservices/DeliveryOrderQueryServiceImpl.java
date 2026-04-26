package com.cargasafe.trip.application.internal.queryservices;

import com.cargasafe.trip.domain.model.entities.DeliveryOrder;
import com.cargasafe.trip.domain.model.queries.DeliveryOrderExistsQuery;
import com.cargasafe.trip.domain.model.queries.GetAllDeliveryOrdersQuery;
import com.cargasafe.trip.domain.services.DeliveryOrderQueryService;
import com.cargasafe.trip.infrastructure.persistence.jpa.DeliveryOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryOrderQueryServiceImpl implements DeliveryOrderQueryService {

    private final DeliveryOrderRepository deliveryOrderRepository;

    public DeliveryOrderQueryServiceImpl(DeliveryOrderRepository deliveryOrderRepository) {
        this.deliveryOrderRepository = deliveryOrderRepository;
    }

    @Override
    public List<DeliveryOrder> handle(GetAllDeliveryOrdersQuery query) {
        return deliveryOrderRepository.findAll();
    }

    @Override
    public boolean handle(DeliveryOrderExistsQuery query) {
        return deliveryOrderRepository.existsById(query.deliveryOrderId());
    }
}
