package com.cargasafe.trip.application.internal.commandservices;

import com.cargasafe.trip.application.internal.outboundservices.ExternalMerchantService;
import com.cargasafe.trip.domain.exceptions.MerchantNotFoundException;
import com.cargasafe.trip.domain.exceptions.OriginPointNotFoundException;
import com.cargasafe.trip.domain.exceptions.TripNotFoundException;
import com.cargasafe.trip.domain.model.aggregates.Trip;
import com.cargasafe.trip.domain.model.commands.CompleteTripCommand;
import com.cargasafe.trip.domain.model.commands.CreateTripCommand;
import com.cargasafe.trip.domain.model.commands.StartTripCommand;
import com.cargasafe.trip.domain.model.events.TripStartedEvent;
import com.cargasafe.trip.domain.services.TripCommandService;
import com.cargasafe.trip.infrastructure.persistence.jpa.OriginPointRepository;
import com.cargasafe.trip.infrastructure.persistence.jpa.TripRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class TripCommandServiceImpl implements TripCommandService {

    private final TripRepository tripRepository;
    private final OriginPointRepository originPointRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ExternalMerchantService externalMerchantService;

    public TripCommandServiceImpl(TripRepository tripRepository,
                                   OriginPointRepository originPointRepository,
                                   ApplicationEventPublisher eventPublisher,
                                   ExternalMerchantService externalMerchantService) {
        this.tripRepository = tripRepository;
        this.originPointRepository = originPointRepository;
        this.eventPublisher = eventPublisher;
        this.externalMerchantService = externalMerchantService;
    }

    @Transactional
    @Override
    public Trip handle(CreateTripCommand command) {
        if (!externalMerchantService.existsById(command.merchantId()))
            throw new MerchantNotFoundException(command.merchantId());

        var originPoint = originPointRepository.findById(command.originPointId())
                .orElseThrow(() -> new OriginPointNotFoundException(command.originPointId()));

        var trip = new Trip(command);
        trip.assignOriginPoint(originPoint);
        return tripRepository.save(trip);
    }

    @Override
    public void handle(StartTripCommand command) {
        var trip = tripRepository.findById(command.tripId())
                .orElseThrow(() -> new TripNotFoundException(command.tripId()));

        trip.startTrip();
        tripRepository.save(trip);
        eventPublisher.publishEvent(new TripStartedEvent(trip.getId()));
    }

    @Transactional
    @Override
    public void handle(CompleteTripCommand command) {
        var trip = tripRepository.findById(command.tripId())
                .orElseThrow(() -> new TripNotFoundException(command.tripId()));

        trip.completeTrip();
        tripRepository.save(trip);
    }
}
