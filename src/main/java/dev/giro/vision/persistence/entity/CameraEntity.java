package dev.giro.vision.persistence.entity;

import dev.giro.vision.camera.domain.Camera;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cameras")
public class CameraEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String host;

    @Column(nullable = false)
    private String type;

    private boolean enabled;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected CameraEntity() {}

    public static CameraEntity from(Camera camera) {
        CameraEntity e = new CameraEntity();
        e.id = camera.id();
        e.name = camera.name();
        e.host = camera.host();
        e.type = camera.type();
        e.enabled = camera.enabled();
        e.createdAt = camera.createdAt();
        return e;
    }

    public Camera toDomain() {
        return new Camera(id, name, host, type, enabled, createdAt);
    }

    public UUID getId() { return id; }
}
