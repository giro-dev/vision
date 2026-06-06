package dev.giro.vision.api;

import dev.giro.vision.camera.domain.Camera;
import dev.giro.vision.camera.domain.CameraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cameras")
@Tag(name = "Cameras", description = "Camera registration and management")
public class CameraController {

    private final CameraService cameraService;

    public CameraController(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a camera")
    public CameraResponse register(@Valid @RequestBody RegisterCameraRequest request) {
        Camera camera = cameraService.register(request.name(), request.host(), request.type());
        return CameraResponse.from(camera);
    }

    @GetMapping
    @Operation(summary = "List all cameras")
    public List<CameraResponse> list() {
        return cameraService.listAll().stream().map(CameraResponse::from).toList();
    }

    @GetMapping("/test")
    @Operation(summary = "Test if a camera host is reachable")
    public ConnectionTestResponse testConnection(@RequestParam String host) {
        boolean reachable = cameraService.testConnection(host);
        return new ConnectionTestResponse(host, reachable);
    }

    public record RegisterCameraRequest(
            @NotBlank String name,
            @NotBlank String host,
            @NotBlank String type
    ) {}

    public record CameraResponse(UUID id, String name, String host, String type, boolean enabled) {
        static CameraResponse from(Camera c) {
            return new CameraResponse(c.id(), c.name(), c.host(), c.type(), c.enabled());
        }
    }

    public record ConnectionTestResponse(String host, boolean reachable) {}
}
