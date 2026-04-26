package com.cargasafe.trip.application.internal.eventhandlers;

import com.cargasafe.trip.application.internal.outboundservices.ExternalMonitoringSessionService;
import com.cargasafe.trip.domain.model.events.TripStartedEvent;
import com.cargasafe.trip.domain.model.valueobjects.DeliveryOrderStatus;
import com.cargasafe.trip.infrastructure.persistence.jpa.DeliveryOrderRepository;
import com.cargasafe.trip.infrastructure.persistence.jpa.TripRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class TripStartedEventHandler {

    private final TripRepository tripRepository;
    private final DeliveryOrderRepository deliveryOrderRepository;
    private final ExternalMonitoringSessionService externalMonitoringSessionService;

    public TripStartedEventHandler(TripRepository tripRepository,
                                    DeliveryOrderRepository deliveryOrderRepository,
                                    ExternalMonitoringSessionService externalMonitoringSessionService) {
        this.tripRepository = tripRepository;
        this.deliveryOrderRepository = deliveryOrderRepository;
        this.externalMonitoringSessionService = externalMonitoringSessionService;
    }

    @EventListener
    public void on(TripStartedEvent event) {
        var maybeTrip = tripRepository.findById(event.tripId());
        if (maybeTrip.isEmpty()) return;

        var trip = maybeTrip.get();
        var deliveryOrders = deliveryOrderRepository.findByTripId(event.tripId());
        deliveryOrders.forEach(o -> o.setStatus(DeliveryOrderStatus.PENDING));
        deliveryOrderRepository.saveAll(deliveryOrders);

        externalMonitoringSessionService.createAndStartMonitoringSession(trip.getId(), trip.getDeviceId());
    }
}
