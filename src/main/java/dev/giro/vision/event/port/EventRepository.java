package dev.giro.vision.event.port;

import dev.giro.vision.event.domain.DetectionEvent;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EventRepository {

    DetectionEvent save(DetectionEvent event);

    List<DetectionEvent> findByCameraId(UUID cameraId);

    List<DetectionEvent> findByType(DetectionEvent.EventType type);

    List<DetectionEvent> findByTimestampBetween(Instant from, Instant to);

    List<DetectionEvent> findAll();
}
