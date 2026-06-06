package dev.giro.vision.camera.port;

import dev.giro.vision.camera.domain.Camera;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CameraRepository {

    Camera save(Camera camera);

    Optional<Camera> findById(UUID id);

    List<Camera> findAll();

    void deleteById(UUID id);
}
