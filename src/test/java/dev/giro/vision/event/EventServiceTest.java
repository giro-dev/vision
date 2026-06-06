package dev.giro.vision.event;

import dev.giro.vision.event.domain.DetectionEvent;
import dev.giro.vision.event.domain.DetectionEvent.EventType;
import dev.giro.vision.event.domain.EventService;
import dev.giro.vision.event.port.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EventServiceTest {

    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventService = new EventService(new InMemoryEventRepository());
    }

    @Test
    void recordAndListEvents() {
        UUID cam = UUID.randomUUID();
        eventService.record(cam, EventType.PERSON_DETECTED, "person-1", 0.95, null);
        eventService.record(cam, EventType.PLATE_READ, "ABC123", 0.90, null);

        assertThat(eventService.listAll()).hasSize(2);
        assertThat(eventService.listByCamera(cam)).hasSize(2);
        assertThat(eventService.listByType(EventType.PLATE_READ)).hasSize(1);
    }

    // ---- stubs ----

    static class InMemoryEventRepository implements EventRepository {
        private final List<DetectionEvent> store = new ArrayList<>();

        @Override
        public DetectionEvent save(DetectionEvent event) {
            store.add(event);
            return event;
        }

        @Override
        public List<DetectionEvent> findByCameraId(UUID cameraId) {
            return store.stream().filter(e -> cameraId.equals(e.cameraId())).toList();
        }

        @Override
        public List<DetectionEvent> findByType(EventType type) {
            return store.stream().filter(e -> type.equals(e.type())).toList();
        }

        @Override
        public List<DetectionEvent> findByTimestampBetween(Instant from, Instant to) {
            return store.stream()
                    .filter(e -> !e.timestamp().isBefore(from) && !e.timestamp().isAfter(to))
                    .toList();
        }

        @Override
        public List<DetectionEvent> findAll() {
            return List.copyOf(store);
        }
    }
}
