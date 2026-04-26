package com.cargasafe.trip.infrastructure.persistence.jpa;

import com.cargasafe.trip.domain.model.aggregates.Trip;
import com.cargasafe.trip.domain.model.valueobjects.TripStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Page<Trip> findByMerchantId(Long merchantId, Pageable pageable);
    Page<Trip> findByMerchantIdAndStatus(Long merchantId, TripStatus status, Pageable pageable);
    Page<Trip> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
    Page<Trip> findByMerchantIdAndCreatedAtBetween(Long merchantId, LocalDateTime from, LocalDateTime to, Pageable pageable);
    Page<Trip> findByMerchantIdAndStatusAndCreatedAtBetween(Long merchantId, TripStatus status, LocalDateTime from, LocalDateTime to, Pageable pageable);
    Page<Trip> findByStatusAndCreatedAtBetween(TripStatus status, LocalDateTime from, LocalDateTime to, Pageable pageable);
}
