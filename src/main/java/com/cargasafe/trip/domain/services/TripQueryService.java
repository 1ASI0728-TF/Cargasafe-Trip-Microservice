package com.cargasafe.trip.domain.services;

import com.cargasafe.trip.domain.model.aggregates.Trip;
import com.cargasafe.trip.domain.model.queries.GetAllTripsQuery;
import com.cargasafe.trip.domain.model.queries.GetTripByIdQuery;
import com.cargasafe.trip.domain.model.valueobjects.TripStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TripQueryService {
    Optional<Trip> handle(GetTripByIdQuery query);
    List<Trip> handle(GetAllTripsQuery query);
    Page<Trip> getByMerchant(Long merchantId, TripStatus status, Instant from, Instant to, Pageable pageable);
    Page<Trip> searchByDateRange(Instant from, Instant to, Long merchantId, TripStatus status, Pageable pageable);
}
