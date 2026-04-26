package com.cargasafe.trip.infrastructure.persistence.jpa;

import com.cargasafe.trip.domain.model.entities.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {
    List<DeliveryOrder> findByTripId(Long tripId);
}
