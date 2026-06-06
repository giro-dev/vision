package dev.giro.vision.camera.domain;

import dev.giro.vision.camera.port.CameraPort;
import dev.giro.vision.camera.port.CameraRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CameraService {

    private final CameraRepository cameraRepository;
    private final CameraPort cameraPort;

    public CameraService(CameraRepository cameraRepository, CameraPort cameraPort) {
        this.cameraRepository = cameraRepository;
        this.cameraPort = cameraPort;
    }

    public Camera register(String name, String host, String type) {
        Camera camera = Camera.create(name, host, type);
        return cameraRepository.save(camera);
    }

    public List<Camera> listAll() {
        return cameraRepository.findAll();
    }

    public byte[] snapshot(UUID cameraId, String user, String password) {
        Camera camera = cameraRepository.findById(cameraId)
                .orElseThrow(() -> new IllegalArgumentException("Camera not found: " + cameraId));
        return cameraPort.captureSnapshot(camera.host(), user, password);
    }

    public boolean testConnection(String host) {
        return cameraPort.isReachable(host);
    }
}
