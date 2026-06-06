package dev.giro.vision.persistence;

import dev.giro.vision.persistence.entity.CameraEntity;
import dev.giro.vision.persistence.entity.PersonEntity;
import dev.giro.vision.persistence.jpa.CameraJpaRepository;
import dev.giro.vision.persistence.jpa.PersonJpaRepository;
import dev.giro.vision.camera.domain.Camera;
import dev.giro.vision.face.domain.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersistenceIntegrationTest {

    @Autowired
    private CameraJpaRepository cameraRepo;

    @Autowired
    private PersonJpaRepository personRepo;

    @Test
    void savesAndRetrievesCamera() {
        Camera cam = Camera.create("Front Door", "192.168.1.100", "reolink");
        cameraRepo.save(CameraEntity.from(cam));

        assertThat(cameraRepo.findAll()).hasSize(1);
        assertThat(cameraRepo.findById(cam.id())).isPresent();
    }

    @Test
    void savesAndRetrievesPerson() {
        Person person = Person.create("Albert");
        personRepo.save(PersonEntity.from(person));

        assertThat(personRepo.findAll()).hasSize(1);
        assertThat(personRepo.findById(person.id()).map(PersonEntity::toDomain).orElseThrow().name())
                .isEqualTo("Albert");
    }
}
