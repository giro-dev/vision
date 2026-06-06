package dev.giro.vision.camera.domain;

import java.time.Instant;
import java.util.UUID;

public record Camera(
        UUID id,
        String name,
        String host,
        String type,
        boolean enabled,
        Instant createdAt
) {

    public static Camera create(String name, String host, String type) {
        return new Camera(UUID.randomUUID(), name, host, type, true, Instant.now());
    }
}
