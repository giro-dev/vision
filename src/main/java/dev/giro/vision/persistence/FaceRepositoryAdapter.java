package dev.giro.vision.persistence;

import dev.giro.vision.face.domain.Face;
import dev.giro.vision.face.port.FaceRepository;
import dev.giro.vision.persistence.entity.FaceEntity;
import dev.giro.vision.persistence.jpa.FaceJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public List<Face> findByEmbeddingNear(float[] embedding, double threshold, int limit) {
        String vectorString = toVectorString(embedding);
        return jpa.findByEmbeddingNear(vectorString, threshold, limit)
                .stream().map(FaceEntity::toDomain).toList();
    }

    @Override
    @Transactional
    public void updatePersonId(UUID oldPersonId, UUID newPersonId) {
        jpa.updatePersonId(oldPersonId, newPersonId);
    }

    private static String toVectorString(float[] vec) {
        if (vec == null || vec.length == 0) return null;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vec.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(vec[i]);
        }
        sb.append(']');
        return sb.toString();
    }
}
