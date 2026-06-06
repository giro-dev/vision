package dev.giro.vision.persistence.entity;

import dev.giro.vision.vehicle.domain.Vehicle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
public class VehicleEntity {

    @Id
    private UUID id;

    @Column(unique = true)
    private String plate;

    private String color;

    private String type;

    private double confidence;

    @Column(name = "detected_at", nullable = false)
    private Instant detectedAt;

    protected VehicleEntity() {}

    public static VehicleEntity from(Vehicle vehicle) {
        VehicleEntity e = new VehicleEntity();
        e.id = vehicle.id();
        e.plate = vehicle.plate();
        e.color = vehicle.color();
        e.type = vehicle.type();
        e.confidence = vehicle.confidence();
        e.detectedAt = vehicle.detectedAt();
        return e;
    }

    public Vehicle toDomain() {
        return new Vehicle(id, plate, color, type, confidence, detectedAt);
    }

    public UUID getId() { return id; }

    public String getPlate() { return plate; }
}
