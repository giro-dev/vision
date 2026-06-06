package dev.giro.vision.vehicle.domain;

import java.time.Instant;
import java.util.UUID;

public record Vehicle(
        UUID id,
        String plate,
        String color,
        String type,
        double confidence,
        Instant detectedAt
) {

    public static Vehicle detected(String plate, String color, String type, double confidence) {
        return new Vehicle(UUID.randomUUID(), plate, color, type, confidence, Instant.now());
    }
}
