package dev.giro.vision.persistence.jpa;

import dev.giro.vision.persistence.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, UUID> {

    Optional<VehicleEntity> findByPlate(String plate);
}
