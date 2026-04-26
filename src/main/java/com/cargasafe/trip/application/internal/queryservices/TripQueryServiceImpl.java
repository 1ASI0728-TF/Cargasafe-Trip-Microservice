package com.cargasafe.trip.application.internal.queryservices;

import com.cargasafe.trip.domain.model.aggregates.Trip;
import com.cargasafe.trip.domain.model.queries.GetAllTripsQuery;
import com.cargasafe.trip.domain.model.queries.GetTripByIdQuery;
import com.cargasafe.trip.domain.model.valueobjects.TripStatus;
import com.cargasafe.trip.domain.services.TripQueryService;
import com.cargasafe.trip.infrastructure.persistence.jpa.TripRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
public class TripQueryServiceImpl implements TripQueryService {

    private final TripRepository tripRepository;

    public TripQueryServiceImpl(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public Optional<Trip> handle(GetTripByIdQuery query) {
        return tripRepository.findById(query.id());
    }

    @Override
    public List<Trip> handle(GetAllTripsQuery query) {
        return tripRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public Page<Trip> getByMerchant(Long merchantId, TripStatus status, Instant from, Instant to, Pageable pageable) {
        boolean hasRange = (from != null && to != null);
        if (hasRange && status != null) {
            return tripRepository.findByMerchantIdAndStatusAndCreatedAtBetween(merchantId, status, toLocal(from), toLocal(to), pageable);
        } else if (hasRange) {
            return tripRepository.findByMerchantIdAndCreatedAtBetween(merchantId, toLocal(from), toLocal(to), pageable);
        } else if (status != null) {
            return tripRepository.findByMerchantIdAndStatus(merchantId, status, pageable);
        } else {
            return tripRepository.findByMerchantId(merchantId, pageable);
        }
    }

    @Override
    public Page<Trip> searchByDateRange(Instant from, Instant to, Long merchantId, TripStatus status, Pageable pageable) {
        if (from == null || to == null) throw new IllegalArgumentException("from/to are required");
        boolean filterMerchant = merchantId != null;
        boolean filterStatus = status != null;

        if (filterMerchant && filterStatus) {
            return tripRepository.findByMerchantIdAndStatusAndCreatedAtBetween(merchantId, status, toLocal(from), toLocal(to), pageable);
        } else if (filterMerchant) {
            return tripRepository.findByMerchantIdAndCreatedAtBetween(merchantId, toLocal(from), toLocal(to), pageable);
        } else if (filterStatus) {
            return tripRepository.findByStatusAndCreatedAtBetween(status, toLocal(from), toLocal(to), pageable);
        } else {
            return tripRepository.findByCreatedAtBetween(toLocal(from), toLocal(to), pageable);
        }
    }

    private LocalDateTime toLocal(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
