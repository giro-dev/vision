package dev.giro.vision.face.port;

import dev.giro.vision.face.domain.Face;

import java.util.List;
import java.util.UUID;

public interface FaceRepository {

    Face save(Face face);

    List<Face> findByPersonId(UUID personId);

    List<Face> findByEmbeddingNear(float[] embedding, double threshold, int limit);

    void updatePersonId(UUID oldPersonId, UUID newPersonId);
}
