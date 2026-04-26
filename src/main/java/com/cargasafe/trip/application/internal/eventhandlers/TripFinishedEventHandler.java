package com.cargasafe.trip.application.internal.eventhandlers;

import com.cargasafe.trip.application.internal.outboundservices.ExternalMonitoringSessionService;
import com.cargasafe.trip.domain.model.events.TripFinishedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class TripFinishedEventHandler {

    private final ExternalMonitoringSessionService externalMonitoringSessionService;

    public TripFinishedEventHandler(ExternalMonitoringSessionService externalMonitoringSessionService) {
        this.externalMonitoringSessionService = externalMonitoringSessionService;
    }

    @EventListener
    public void on(TripFinishedEvent event) {
        externalMonitoringSessionService.endMonitoringSessionByTripId(event.tripId());
    }
}
