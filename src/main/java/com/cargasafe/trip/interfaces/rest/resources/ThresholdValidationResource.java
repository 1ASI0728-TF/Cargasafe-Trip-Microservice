package com.cargasafe.trip.interfaces.rest.resources;

import java.util.List;

public record ThresholdValidationResource(Long deliveryOrderId, List<String> thresholdTypes) {
}
