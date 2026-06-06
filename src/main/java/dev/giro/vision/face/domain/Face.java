package dev.giro.vision.face.domain;

import java.time.Instant;
import java.util.UUID;

public record Face(
        UUID id,
        UUID personId,
        float[] embedding,
        String imageUrl,
        double confidence,
        Instant detectedAt
) {

    public static Face detected(UUID personId, float[] embedding, String imageUrl, double confidence) {
        return new Face(UUID.randomUUID(), personId, embedding, imageUrl, confidence, Instant.now());
    }
}
