package dev.giro.vision.persistence.entity;

import dev.giro.vision.event.domain.DetectionEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "detection_events")
public class DetectionEventEntity {

    @Id
    private UUID id;

    @Column(name = "camera_id")
    private UUID cameraId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DetectionEvent.EventType type;

    private String label;

    private double confidence;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private Instant timestamp;

    protected DetectionEventEntity() {}

    public static DetectionEventEntity from(DetectionEvent event) {
        DetectionEventEntity e = new DetectionEventEntity();
        e.id = event.id();
        e.cameraId = event.cameraId();
        e.type = event.type();
        e.label = event.label();
        e.confidence = event.confidence();
        e.imageUrl = event.imageUrl();
        e.timestamp = event.timestamp();
        return e;
    }

    public DetectionEvent toDomain() {
        return new DetectionEvent(id, cameraId, type, label, confidence, imageUrl, timestamp);
    }

    public UUID getId() { return id; }

    public UUID getCameraId() { return cameraId; }

    public DetectionEvent.EventType getType() { return type; }

    public Instant getTimestamp() { return timestamp; }
}
