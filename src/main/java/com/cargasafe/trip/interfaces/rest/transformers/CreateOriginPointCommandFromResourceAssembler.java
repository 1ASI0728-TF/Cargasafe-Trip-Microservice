package com.cargasafe.trip.interfaces.rest.transformers;

import com.cargasafe.trip.domain.model.commands.CreateOriginPointCommand;
import com.cargasafe.trip.interfaces.rest.resources.CreateOriginPointResource;

public final class CreateOriginPointCommandFromResourceAssembler {

    public static CreateOriginPointCommand toCommandFromResource(CreateOriginPointResource resource) {
        return new CreateOriginPointCommand(resource.name(), resource.address(), resource.latitude(), resource.longitude());
    }
}
