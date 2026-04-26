package com.cargasafe.trip.application.internal.queryservices;

import com.cargasafe.trip.domain.model.entities.OriginPoint;
import com.cargasafe.trip.domain.model.queries.GetAllOriginPointsQuery;
import com.cargasafe.trip.domain.services.OriginPointQueryService;
import com.cargasafe.trip.infrastructure.persistence.jpa.OriginPointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OriginPointQueryServiceImpl implements OriginPointQueryService {

    private final OriginPointRepository originPointRepository;

    public OriginPointQueryServiceImpl(OriginPointRepository originPointRepository) {
        this.originPointRepository = originPointRepository;
    }

    @Override
    public List<OriginPoint> handle(GetAllOriginPointsQuery query) {
        return originPointRepository.findAll();
    }
}
