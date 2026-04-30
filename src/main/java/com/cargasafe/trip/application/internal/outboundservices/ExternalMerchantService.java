package com.cargasafe.trip.application.internal.outboundservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class ExternalMerchantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalMerchantService.class);

    private final RestClient restClient;

    public ExternalMerchantService(
            @Value("${integrations.merchant-service.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public boolean existsById(Long merchantId) {
        try {
            restClient.get()
                    .uri("/api/v1/merchants/{id}", merchantId)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (RestClientException e) {
            LOGGER.warn("Merchant with id={} not found or merchant-service unavailable: {}", merchantId, e.getMessage());
            return false;
        }
    }
}