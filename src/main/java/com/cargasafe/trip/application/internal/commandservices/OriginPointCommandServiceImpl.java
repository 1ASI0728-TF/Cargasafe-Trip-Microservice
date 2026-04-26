package com.cargasafe.trip.application.internal.commandservices;

import com.cargasafe.trip.domain.model.commands.CreateOriginPointCommand;
import com.cargasafe.trip.domain.model.entities.OriginPoint;
import com.cargasafe.trip.domain.services.OriginPointCommandService;
import com.cargasafe.trip.infrastructure.persistence.jpa.OriginPointRepository;
import org.springframework.stereotype.Service;

@Service
public class OriginPointCommandServiceImpl implements OriginPointCommandService {

    private final OriginPointRepository originPointRepository;

    public OriginPointCommandServiceImpl(OriginPointRepository originPointRepository) {
        this.originPointRepository = originPointRepository;
    }

    @Override
    public OriginPoint handle(CreateOriginPointCommand command) {
        return originPointRepository.save(new OriginPoint(command));
    }
}
