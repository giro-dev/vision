package dev.giro.vision.api;

import dev.giro.vision.event.domain.DetectionEvent;
import dev.giro.vision.event.domain.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Events", description = "Detection event history")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    @Operation(summary = "List all detection events")
    public List<EventResponse> list(@RequestParam(required = false) UUID cameraId,
                                    @RequestParam(required = false) DetectionEvent.EventType type) {
        List<DetectionEvent> events;
        if (cameraId != null) {
            events = eventService.listByCamera(cameraId);
        } else if (type != null) {
            events = eventService.listByType(type);
        } else {
            events = eventService.listAll();
        }
        return events.stream().map(EventResponse::from).toList();
    }

    public record EventResponse(UUID id, UUID cameraId, String type,
                                 String label, double confidence, String timestamp) {
        static EventResponse from(DetectionEvent e) {
            return new EventResponse(e.id(), e.cameraId(), e.type().name(),
                    e.label(), e.confidence(), e.timestamp().toString());
        }
    }
}
