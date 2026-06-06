package dev.giro.vision.persistence.jpa;

import dev.giro.vision.event.domain.DetectionEvent;
import dev.giro.vision.persistence.entity.DetectionEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface DetectionEventJpaRepository extends JpaRepository<DetectionEventEntity, UUID> {

    List<DetectionEventEntity> findByCameraId(UUID cameraId);

    List<DetectionEventEntity> findByType(DetectionEvent.EventType type);

    List<DetectionEventEntity> findByTimestampBetween(Instant from, Instant to);
}
