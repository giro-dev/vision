package dev.giro.vision.face.domain;

import java.time.Instant;
import java.util.UUID;

public record Person(
        UUID id,
        String name,
        Instant createdAt
) {

    public static Person create(String name) {
        return new Person(UUID.randomUUID(), name, Instant.now());
    }
}
