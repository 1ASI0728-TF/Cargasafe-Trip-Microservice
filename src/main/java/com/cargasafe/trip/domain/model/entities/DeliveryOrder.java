package com.cargasafe.trip.domain.model.entities;

import com.cargasafe.trip.domain.model.aggregates.Trip;
import com.cargasafe.trip.domain.model.valueobjects.DeliveryOrderStatus;
import com.cargasafe.trip.domain.model.valueobjects.Location;
import com.cargasafe.trip.domain.model.valueobjects.OrderThresholds;
import com.cargasafe.trip.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@Entity
public class DeliveryOrder extends AuditableModel {

    private String clientEmail;
    private Long sequenceOrder;
    private LocalDateTime arrivalAt;

    @Embedded
    private OrderThresholds orderThresholds;

    @Embedded
    @NotNull
    private Location location;

    @Enumerated(EnumType.STRING)
    @NotNull
    private DeliveryOrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public DeliveryOrder() {
        this.status = DeliveryOrderStatus.PENDING;
    }

    public void markAsDelivered() {
        this.status = DeliveryOrderStatus.DELIVERED;
        this.arrivalAt = LocalDateTime.now();
    }

    public List<String> validateThresholds(Double temperature, Double humidity) {
        if (this.orderThresholds == null) {
            return List.of();
        }

        var alerts = new ArrayList<String>();
        var t = this.orderThresholds;

        if (temperature != null) {
            if ((t.minTemperature() != null && temperature < t.minTemperature()) ||
                    (t.maxTemperature() != null && temperature > t.maxTemperature())) {
                alerts.add("TEMPERATURE");
            }
        }

        if (humidity != null) {
            if ((t.minHumidity() != null && humidity < t.minHumidity()) ||
                    (t.maxHumidity() != null && humidity > t.maxHumidity())) {
                alerts.add("HUMIDITY");
            }
        }

        return alerts;
    }
}
