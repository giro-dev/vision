package dev.giro.vision.persistence.jpa;

import dev.giro.vision.persistence.entity.FaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FaceJpaRepository extends JpaRepository<FaceEntity, UUID> {

    List<FaceEntity> findByPersonId(UUID personId);
}
