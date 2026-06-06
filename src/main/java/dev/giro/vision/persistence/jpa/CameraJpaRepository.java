package dev.giro.vision.persistence.jpa;

import dev.giro.vision.persistence.entity.CameraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CameraJpaRepository extends JpaRepository<CameraEntity, UUID> {
}
