package dev.giro.vision.persistence;

import dev.giro.vision.camera.domain.Camera;
import dev.giro.vision.camera.port.CameraRepository;
import dev.giro.vision.persistence.entity.CameraEntity;
import dev.giro.vision.persistence.jpa.CameraJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CameraRepositoryAdapter implements CameraRepository {

    private final CameraJpaRepository jpa;

    public CameraRepositoryAdapter(CameraJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Camera save(Camera camera) {
        jpa.save(CameraEntity.from(camera));
        return camera;
    }

    @Override
    public Optional<Camera> findById(UUID id) {
        return jpa.findById(id).map(CameraEntity::toDomain);
    }

    @Override
    public List<Camera> findAll() {
        return jpa.findAll().stream().map(CameraEntity::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
