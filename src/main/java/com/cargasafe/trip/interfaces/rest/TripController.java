package com.cargasafe.trip.interfaces.rest;

import com.cargasafe.trip.domain.exceptions.MerchantNotFoundException;
import com.cargasafe.trip.domain.exceptions.OriginPointNotFoundException;
import com.cargasafe.trip.domain.exceptions.TripNotFoundException;
import com.cargasafe.trip.domain.model.commands.CompleteTripCommand;
import com.cargasafe.trip.domain.model.commands.StartTripCommand;
import com.cargasafe.trip.domain.model.queries.GetAllTripsQuery;
import com.cargasafe.trip.domain.model.queries.GetTripByIdQuery;
import com.cargasafe.trip.domain.services.TripCommandService;
import com.cargasafe.trip.domain.services.TripQueryService;
import com.cargasafe.trip.interfaces.rest.resources.CreateTripResource;
import com.cargasafe.trip.interfaces.rest.resources.ThresholdValidationResource;
import com.cargasafe.trip.interfaces.rest.resources.TripResource;
import com.cargasafe.trip.interfaces.rest.transformers.CreateTripCommandFromResourceAssembler;
import com.cargasafe.trip.interfaces.rest.transformers.TripResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/trips")
@Tag(name = "Trips", description = "Endpoint for managing trips")
public class TripController {

    private final TripQueryService tripQueryService;
    private final TripCommandService tripCommandService;

    public TripController(TripQueryService tripQueryService, TripCommandService tripCommandService) {
        this.tripQueryService = tripQueryService;
        this.tripCommandService = tripCommandService;
    }

    @Operation(summary = "List all trips")
    @GetMapping
    public List<TripResource> listAll() {
        return tripQueryService.handle(new GetAllTripsQuery()).stream()
                .map(TripResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
    }

    @Operation(summary = "Get a trip by ID")
    @GetMapping("/{tripId}")
    public ResponseEntity<TripResource> getById(@PathVariable Long tripId) {
        return tripQueryService.handle(new GetTripByIdQuery(tripId))
                .map(t -> ResponseEntity.ok(TripResourceFromEntityAssembler.toResourceFromEntity(t)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "List trips by merchant")
    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<TripResource>> getByMerchant(@PathVariable Long merchantId) {
        var page = tripQueryService.getByMerchant(merchantId, null, null, null,
                org.springframework.data.domain.Pageable.unpaged());
        return ResponseEntity.ok(page.map(TripResourceFromEntityAssembler::toResourceFromEntity).getContent());
    }

    @Operation(summary = "Search trips by date range")
    @GetMapping("/search")
    public ResponseEntity<List<TripResource>> searchByDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {

        if (from.isAfter(to)) throw new IllegalArgumentException("from cannot be after to");

        var page = tripQueryService.searchByDateRange(from, to, null, null,
                org.springframework.data.domain.Pageable.unpaged());
        return ResponseEntity.ok(page.map(TripResourceFromEntityAssembler::toResourceFromEntity).getContent());
    }

    @Operation(summary = "Create trip")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateTripResource resource) {
        try {
            tripCommandService.handle(CreateTripCommandFromResourceAssembler.toCommandFromResource(resource));
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (MerchantNotFoundException | OriginPointNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @Operation(summary = "Start trip")
    @PostMapping("/{tripId}/start")
    public ResponseEntity<?> startTrip(@PathVariable Long tripId) {
        try {
            tripCommandService.handle(new StartTripCommand(tripId));
            return ResponseEntity.ok().build();
        } catch (TripNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Complete trip")
    @PostMapping("/{tripId}/complete")
    public ResponseEntity<?> completeTrip(@PathVariable Long tripId) {
        try {
            tripCommandService.handle(new CompleteTripCommand(tripId));
            return ResponseEntity.ok().build();
        } catch (TripNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ACL endpoint consumed by monitoring-service
    @Operation(summary = "Validate thresholds for all delivery orders in a trip")
    @GetMapping("/{tripId}/threshold-validations")
    public ResponseEntity<?> validateThresholds(
            @PathVariable Long tripId,
            @RequestParam(required = false) Double temperature,
            @RequestParam(required = false) Double humidity) {

        return tripQueryService.handle(new GetTripByIdQuery(tripId))
                .map(trip -> {
                    var validations = trip.getDeliveryOrderList().stream()
                            .map(o -> new ThresholdValidationResource(o.getId(), o.validateThresholds(temperature, humidity)))
                            .toList();
                    return ResponseEntity.ok(validations);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
