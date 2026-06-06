package dev.giro.vision.event.domain;

import java.time.Instant;
import java.util.UUID;

public record DetectionEvent(
        UUID id,
        UUID cameraId,
        EventType type,
        String label,
        double confidence,
        String imageUrl,
        Instant timestamp
) {

    public enum EventType {
        PERSON_DETECTED,
        FACE_RECOGNIZED,
        VEHICLE_DETECTED,
        PLATE_READ,
        UNKNOWN
    }

    public static DetectionEvent create(UUID cameraId, EventType type,
                                        String label, double confidence, String imageUrl) {
        return new DetectionEvent(
                UUID.randomUUID(), cameraId, type, label, confidence, imageUrl, Instant.now()
        );
    }
}
