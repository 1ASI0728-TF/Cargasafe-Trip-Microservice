package com.cargasafe.trip.interfaces.rest;

import com.cargasafe.trip.domain.exceptions.DeliveryOrderNotFoundException;
import com.cargasafe.trip.domain.exceptions.TripNotFoundException;
import com.cargasafe.trip.domain.model.commands.DeliverDeliveryOrderCommand;
import com.cargasafe.trip.domain.model.queries.DeliveryOrderExistsQuery;
import com.cargasafe.trip.domain.model.queries.GetAllDeliveryOrdersQuery;
import com.cargasafe.trip.domain.services.DeliveryOrderCommandService;
import com.cargasafe.trip.domain.services.DeliveryOrderQueryService;
import com.cargasafe.trip.interfaces.rest.resources.DeliveryOrderResource;
import com.cargasafe.trip.interfaces.rest.transformers.DeliveryOrderResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/delivery-orders")
@Tag(name = "Delivery Orders", description = "Endpoint for managing delivery orders")
public class DeliveryOrderController {

    private final DeliveryOrderQueryService deliveryOrderQueryService;
    private final DeliveryOrderCommandService deliveryOrderCommandService;

    public DeliveryOrderController(DeliveryOrderQueryService deliveryOrderQueryService,
                                    DeliveryOrderCommandService deliveryOrderCommandService) {
        this.deliveryOrderQueryService = deliveryOrderQueryService;
        this.deliveryOrderCommandService = deliveryOrderCommandService;
    }

    @GetMapping
    public ResponseEntity<List<DeliveryOrderResource>> getAll() {
        var resources = deliveryOrderQueryService.handle(new GetAllDeliveryOrdersQuery()).stream()
                .map(DeliveryOrderResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping("/{id}/delivery")
    public ResponseEntity<?> markAsDelivered(@PathVariable Long id) {
        try {
            deliveryOrderCommandService.handle(new DeliverDeliveryOrderCommand(id));
            return ResponseEntity.ok().build();
        } catch (DeliveryOrderNotFoundException | TripNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ACL endpoint consumed by monitoring-service
    @Operation(summary = "Check if a delivery order exists")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Map<String, Boolean>> exists(@PathVariable Long id) {
        boolean exists = deliveryOrderQueryService.handle(new DeliveryOrderExistsQuery(id));
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
