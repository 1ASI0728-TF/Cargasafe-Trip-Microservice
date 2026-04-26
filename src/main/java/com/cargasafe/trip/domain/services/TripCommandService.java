package com.cargasafe.trip.domain.services;

import com.cargasafe.trip.domain.model.aggregates.Trip;
import com.cargasafe.trip.domain.model.commands.CompleteTripCommand;
import com.cargasafe.trip.domain.model.commands.CreateTripCommand;
import com.cargasafe.trip.domain.model.commands.StartTripCommand;

public interface TripCommandService {
    Trip handle(CreateTripCommand command);
    void handle(StartTripCommand command);
    void handle(CompleteTripCommand command);
}
