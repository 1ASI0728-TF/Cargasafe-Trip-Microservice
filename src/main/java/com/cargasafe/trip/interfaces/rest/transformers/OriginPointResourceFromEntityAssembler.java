package com.cargasafe.trip.interfaces.rest.transformers;

import com.cargasafe.trip.domain.model.entities.OriginPoint;
import com.cargasafe.trip.interfaces.rest.resources.OriginPointResource;

public final class OriginPointResourceFromEntityAssembler {

    public static OriginPointResource toResourceFromEntity(OriginPoint entity) {
        return new OriginPointResource(
                entity.getId(),
                entity.getName(),
                entity.getLocation().address(),
                entity.getLocation().latitude(),
                entity.getLocation().longitude()
        );
    }
}
