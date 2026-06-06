package dev.giro.vision.persistence;

import dev.giro.vision.face.domain.Face;
import dev.giro.vision.face.port.FaceRepository;
import dev.giro.vision.persistence.entity.FaceEntity;
import dev.giro.vision.persistence.jpa.FaceJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class FaceRepositoryAdapter implements FaceRepository {

    private final FaceJpaRepository jpa;

    public FaceRepositoryAdapter(FaceJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Face save(Face face) {
        jpa.save(FaceEntity.from(face));
        return face;
    }

    @Override
    public List<Face> findByPersonId(UUID personId) {
        return jpa.findByPersonId(personId).stream().map(FaceEntity::toDomain).toList();
    }
}
