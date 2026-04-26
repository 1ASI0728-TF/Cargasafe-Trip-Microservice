package com.cargasafe.trip.application.internal.outboundservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Service
public class ExternalMonitoringSessionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalMonitoringSessionService.class);

    private final RestClient restClient;

    public ExternalMonitoringSessionService(
            @Value("${integrations.monitoring-service.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public void createAndStartMonitoringSession(Long tripId, Long deviceId) {
        try {
            restClient.post()
                    .uri("/api/v1/monitoring-sessions")
                    .body(Map.of("tripId", tripId, "deviceId", deviceId))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            LOGGER.error("Failed to create monitoring session for tripId={}: {}", tripId, e.getMessage());
        }
    }

    public void endMonitoringSessionByTripId(Long tripId) {
        try {
            restClient.patch()
                    .uri("/api/v1/monitoring-sessions/trip/{tripId}/end", tripId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            LOGGER.error("Failed to end monitoring session for tripId={}: {}", tripId, e.getMessage());
        }
    }
}
