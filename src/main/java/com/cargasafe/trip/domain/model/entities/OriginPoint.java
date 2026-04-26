package com.cargasafe.trip.domain.model.entities;

import com.cargasafe.trip.domain.model.commands.CreateOriginPointCommand;
import com.cargasafe.trip.domain.model.valueobjects.Location;
import com.cargasafe.trip.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class OriginPoint extends AuditableModel {

    private String name;

    @Column(columnDefinition = "TEXT")
    @Embedded
    private Location location;

    public OriginPoint(CreateOriginPointCommand command) {
        this.name = command.name();
        this.location = new Location(command.address(), command.latitude(), command.longitude());
    }
}
