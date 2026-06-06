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
        e.imageUrl = face.imageUrl();
        e.confidence = face.confidence();
        e.detectedAt = face.detectedAt();
        return e;
    }

    public Face toDomain() {
        return new Face(id, personId, null, imageUrl, confidence, detectedAt);
    }

    public UUID getId() { return id; }

    public UUID getPersonId() { return personId; }
}
