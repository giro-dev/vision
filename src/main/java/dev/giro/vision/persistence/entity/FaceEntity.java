package dev.giro.vision.persistence.entity;

import dev.giro.vision.face.domain.Face;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "faces")
public class FaceEntity {

    @Id
    private UUID id;

    @Column(name = "person_id")
    private UUID personId;

    @Column(name = "embedding")
    private String embedding;

    @Column(name = "image_url")
    private String imageUrl;

    private double confidence;

    @Column(name = "detected_at", nullable = false)
    private Instant detectedAt;

    protected FaceEntity() {}

    public static FaceEntity from(Face face) {
        FaceEntity e = new FaceEntity();
        e.id = face.id();
        e.personId = face.personId();
        e.embedding = toVectorString(face.embedding());
        e.imageUrl = face.imageUrl();
        e.confidence = face.confidence();
        e.detectedAt = face.detectedAt();
        return e;
    }

    public Face toDomain() {
        return new Face(id, personId, parseVector(embedding), imageUrl, confidence, detectedAt);
    }

    public UUID getId() { return id; }

    public UUID getPersonId() { return personId; }

    private static String toVectorString(float[] vec) {
        if (vec == null || vec.length == 0) return null;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vec.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(vec[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    private static float[] parseVector(String s) {
        if (s == null || s.isBlank()) return null;
        String inner = s.substring(1, s.length() - 1);
        String[] parts = inner.split(",");
        float[] result = new float[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Float.parseFloat(parts[i]);
        }
        return result;
    }
}
