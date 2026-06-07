package dev.giro.vision.persistence.jpa;

import dev.giro.vision.persistence.entity.FaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FaceJpaRepository extends JpaRepository<FaceEntity, UUID> {

    List<FaceEntity> findByPersonId(UUID personId);

    @Query(value = """
            SELECT * FROM faces
            WHERE embedding IS NOT NULL
              AND 1 - (embedding <=> CAST(:embedding AS vector)) >= :threshold
            ORDER BY embedding <=> CAST(:embedding AS vector)
            LIMIT :maxResults
            """, nativeQuery = true)
    List<FaceEntity> findByEmbeddingNear(@Param("embedding") String embedding,
                                         @Param("threshold") double threshold,
                                         @Param("maxResults") int maxResults);

    @Modifying
    @Query("UPDATE FaceEntity f SET f.personId = :newPersonId WHERE f.personId = :oldPersonId")
    void updatePersonId(@Param("oldPersonId") UUID oldPersonId,
                        @Param("newPersonId") UUID newPersonId);
}
