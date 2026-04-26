package com.cargasafe.trip.domain.services;

import com.cargasafe.trip.domain.model.commands.CreateOriginPointCommand;
import com.cargasafe.trip.domain.model.entities.OriginPoint;

public interface OriginPointCommandService {
    OriginPoint handle(CreateOriginPointCommand command);
}
