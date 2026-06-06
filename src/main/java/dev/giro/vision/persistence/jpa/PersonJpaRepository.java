package dev.giro.vision.persistence.jpa;

import dev.giro.vision.persistence.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonJpaRepository extends JpaRepository<PersonEntity, UUID> {
}
