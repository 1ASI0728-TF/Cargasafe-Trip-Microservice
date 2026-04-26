package com.cargasafe.trip.interfaces.rest;

import com.cargasafe.trip.domain.model.queries.GetAllOriginPointsQuery;
import com.cargasafe.trip.domain.services.OriginPointCommandService;
import com.cargasafe.trip.domain.services.OriginPointQueryService;
import com.cargasafe.trip.interfaces.rest.resources.CreateOriginPointResource;
import com.cargasafe.trip.interfaces.rest.resources.OriginPointResource;
import com.cargasafe.trip.interfaces.rest.transformers.CreateOriginPointCommandFromResourceAssembler;
import com.cargasafe.trip.interfaces.rest.transformers.OriginPointResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/origin-points")
@Tag(name = "Origin Points", description = "Endpoint for managing origin points")
public class OriginPointController {

    private final OriginPointQueryService originPointQueryService;
    private final OriginPointCommandService originPointCommandService;

    public OriginPointController(OriginPointQueryService originPointQueryService,
                                  OriginPointCommandService originPointCommandService) {
        this.originPointQueryService = originPointQueryService;
        this.originPointCommandService = originPointCommandService;
    }

    @GetMapping
    public ResponseEntity<List<OriginPointResource>> getAll() {
        var resources = originPointQueryService.handle(new GetAllOriginPointsQuery()).stream()
                .map(OriginPointResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    public ResponseEntity<OriginPointResource> create(@RequestBody CreateOriginPointResource resource) {
        var command = CreateOriginPointCommandFromResourceAssembler.toCommandFromResource(resource);
        var originPoint = originPointCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OriginPointResourceFromEntityAssembler.toResourceFromEntity(originPoint));
    }
}
