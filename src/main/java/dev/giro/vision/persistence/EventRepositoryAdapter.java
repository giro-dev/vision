package dev.giro.vision.persistence;

import dev.giro.vision.event.domain.DetectionEvent;
import dev.giro.vision.event.port.EventRepository;
import dev.giro.vision.persistence.entity.DetectionEventEntity;
import dev.giro.vision.persistence.jpa.DetectionEventJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class EventRepositoryAdapter implements EventRepository {

    private final DetectionEventJpaRepository jpa;

    public EventRepositoryAdapter(DetectionEventJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public DetectionEvent save(DetectionEvent event) {
        jpa.save(DetectionEventEntity.from(event));
        return event;
    }

    @Override
    public List<DetectionEvent> findByCameraId(UUID cameraId) {
        return jpa.findByCameraId(cameraId).stream().map(DetectionEventEntity::toDomain).toList();
    }

    @Override
    public List<DetectionEvent> findByType(DetectionEvent.EventType type) {
        return jpa.findByType(type).stream().map(DetectionEventEntity::toDomain).toList();
    }

    @Override
    public List<DetectionEvent> findByTimestampBetween(Instant from, Instant to) {
        return jpa.findByTimestampBetween(from, to).stream().map(DetectionEventEntity::toDomain).toList();
    }

    @Override
    public List<DetectionEvent> findAll() {
        return jpa.findAll().stream().map(DetectionEventEntity::toDomain).toList();
    }
}
