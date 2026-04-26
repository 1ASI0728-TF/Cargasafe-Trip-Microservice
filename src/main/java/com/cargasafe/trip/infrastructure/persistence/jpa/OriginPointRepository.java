package com.cargasafe.trip.infrastructure.persistence.jpa;

import com.cargasafe.trip.domain.model.entities.OriginPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OriginPointRepository extends JpaRepository<OriginPoint, Long> {
}
