package com.cargasafe.trip.application.internal.commandservices;

import com.cargasafe.trip.domain.exceptions.DeliveryOrderNotFoundException;
import com.cargasafe.trip.domain.exceptions.TripNotFoundException;
import com.cargasafe.trip.domain.model.commands.DeliverDeliveryOrderCommand;
import com.cargasafe.trip.domain.model.events.TripFinishedEvent;
import com.cargasafe.trip.domain.services.DeliveryOrderCommandService;
import com.cargasafe.trip.infrastructure.persistence.jpa.DeliveryOrderRepository;
import com.cargasafe.trip.infrastructure.persistence.jpa.TripRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class DeliveryOrderCommandServiceImpl implements DeliveryOrderCommandService {

    private final DeliveryOrderRepository deliveryOrderRepository;
    private final TripRepository tripRepository;
    private final ApplicationEventPublisher eventPublisher;

    public DeliveryOrderCommandServiceImpl(DeliveryOrderRepository deliveryOrderRepository,
                                            TripRepository tripRepository,
                                            ApplicationEventPublisher eventPublisher) {
        this.deliveryOrderRepository = deliveryOrderRepository;
        this.tripRepository = tripRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public void handle(DeliverDeliveryOrderCommand command) {
        var deliveryOrder = deliveryOrderRepository.findById(command.deliveryOrderId())
                .orElseThrow(() -> new DeliveryOrderNotFoundException(command.deliveryOrderId()));

        deliveryOrder.markAsDelivered();
        deliveryOrderRepository.save(deliveryOrder);

        var trip = tripRepository.findById(deliveryOrder.getTrip().getId())
                .orElseThrow(() -> new TripNotFoundException(deliveryOrder.getTrip().getId()));

        if (trip.canCompleteTrip()) {
            trip.completeTrip();
            tripRepository.save(trip);
            eventPublisher.publishEvent(new TripFinishedEvent(trip.getId()));
        }
    }
}
