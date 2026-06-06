package dev.giro.vision.event.domain;

import dev.giro.vision.event.port.EventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public DetectionEvent record(UUID cameraId, DetectionEvent.EventType type,
                                 String label, double confidence, String imageUrl) {
        DetectionEvent event = DetectionEvent.create(cameraId, type, label, confidence, imageUrl);
        return eventRepository.save(event);
    }

    public List<DetectionEvent> listAll() {
        return eventRepository.findAll();
    }

    public List<DetectionEvent> listByCamera(UUID cameraId) {
        return eventRepository.findByCameraId(cameraId);
    }

    public List<DetectionEvent> listByType(DetectionEvent.EventType type) {
        return eventRepository.findByType(type);
    }

    public List<DetectionEvent> listByTimeRange(Instant from, Instant to) {
        return eventRepository.findByTimestampBetween(from, to);
    }
}
