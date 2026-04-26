package com.cargasafe.trip.domain.services;

import com.cargasafe.trip.domain.model.entities.OriginPoint;
import com.cargasafe.trip.domain.model.queries.GetAllOriginPointsQuery;

import java.util.List;

public interface OriginPointQueryService {
    List<OriginPoint> handle(GetAllOriginPointsQuery query);
}
