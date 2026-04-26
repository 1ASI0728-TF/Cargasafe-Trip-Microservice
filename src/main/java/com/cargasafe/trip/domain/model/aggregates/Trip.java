package com.cargasafe.trip.domain.model.aggregates;

import com.cargasafe.trip.domain.model.commands.CreateTripCommand;
import com.cargasafe.trip.domain.model.entities.DeliveryOrder;
import com.cargasafe.trip.domain.model.entities.OriginPoint;
import com.cargasafe.trip.domain.model.valueobjects.DeliveryOrderStatus;
import com.cargasafe.trip.domain.model.valueobjects.TripStatus;
import com.cargasafe.trip.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Trip extends AuditableAbstractAggregateRoot<Trip> {

    private Long merchantId;
    private Long driverId;
    private Long deviceId;
    private Long vehicleId;

    @Enumerated(EnumType.STRING)
    private TripStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryOrder> deliveryOrderList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private OriginPoint originPoint;

    public Trip(CreateTripCommand command) {
        this.merchantId = command.merchantId();
        this.driverId = command.driverId();
        this.deviceId = command.deviceId();
        this.vehicleId = command.vehicleId();
        this.status = TripStatus.CREATED;
        command.deliveryOrderList().forEach(o -> o.setTrip(this));
        this.deliveryOrderList = command.deliveryOrderList();
    }

    public void assignOriginPoint(OriginPoint originPoint) {
        this.originPoint = originPoint;
    }

    public void addDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrderList.add(deliveryOrder);
        deliveryOrder.setTrip(this);
    }

    public void startTrip() {
        this.status = TripStatus.IN_PROGRESS;
        this.startedAt = LocalDateTime.now();
    }

    public boolean canCompleteTrip() {
        return this.status == TripStatus.IN_PROGRESS &&
                this.deliveryOrderList.stream().noneMatch(o -> o.getStatus() == DeliveryOrderStatus.IN_PROGRESS);
    }

    public void completeTrip() {
        if (!canCompleteTrip()) return;
        this.status = TripStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
}
