package dev.giro.vision.face;

import dev.giro.vision.face.domain.Face;
import dev.giro.vision.face.domain.FaceService;
import dev.giro.vision.face.domain.Person;
import dev.giro.vision.face.port.FaceRecognitionPort;
import dev.giro.vision.face.port.FaceRepository;
import dev.giro.vision.face.port.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FaceServiceTest {

    private FaceService faceService;
    private InMemoryPersonRepository personRepo;

    @BeforeEach
    void setUp() {
        personRepo = new InMemoryPersonRepository();
        FaceRecognitionPort recognitionPort = new StubFaceRecognitionPort();
        FaceRepository faceRepo = new InMemoryFaceRepository();
        faceService = new FaceService(recognitionPort, faceRepo, personRepo);
    }

    @Test
    void registerAndListPersons() {
        faceService.registerPerson("Alice");
        faceService.registerPerson("Bob");

        List<Person> persons = faceService.listPersons();
        assertThat(persons).hasSize(2);
        assertThat(persons).extracting(Person::name).containsExactlyInAnyOrder("Alice", "Bob");
    }

    @Test
    void getPersonThrowsWhenNotFound() {
        assertThatThrownBy(() -> faceService.getPerson(UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deletePerson() {
        Person p = faceService.registerPerson("Charlie");
        faceService.deletePerson(p.id());
        assertThat(faceService.listPersons()).isEmpty();
    }

    @Test
    void detectReturnsFaces() {
        List<Face> faces = faceService.detect(new byte[]{1, 2, 3});
        assertThat(faces).hasSize(1);
    }

    @Test
    void updatePerson() {
        Person p = faceService.registerPerson("Old Name");
        Person updated = faceService.updatePerson(p.id(), "New Name");
        assertThat(updated.name()).isEqualTo("New Name");
        assertThat(updated.id()).isEqualTo(p.id());
    }

    @Test
    void mergePersons() {
        Person target = faceService.registerPerson("Target");
        Person source = faceService.registerPerson("Source");
        Person result = faceService.mergePersons(target.id(), source.id());
        assertThat(result.id()).isEqualTo(target.id());
        assertThat(faceService.listPersons()).hasSize(1);
    }

    @Test
    void recognizeReturnsEmptyWhenNoMatch() {
        Optional<Person> result = faceService.recognize(new byte[]{1, 2, 3});
        assertThat(result).isEmpty();
    }

    // ---- stubs ----

    static class StubFaceRecognitionPort implements FaceRecognitionPort {
        @Override
        public List<Face> detectFaces(byte[] image) {
            return List.of(Face.detected(null, new float[128], null, 0.95));
        }

        @Override
        public float[] generateEmbedding(byte[] faceImage) {
            return new float[128];
        }

        @Override
        public double compareFaces(float[] e1, float[] e2) {
            return 0.99;
        }
    }

    static class InMemoryPersonRepository implements PersonRepository {
        private final java.util.Map<UUID, Person> store = new java.util.LinkedHashMap<>();

        @Override
        public Person save(Person person) {
            store.put(person.id(), person);
            return person;
        }

        @Override
        public Optional<Person> findById(UUID id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public List<Person> findAll() {
            return List.copyOf(store.values());
        }

        @Override
        public void deleteById(UUID id) {
            store.remove(id);
        }
    }

    static class InMemoryFaceRepository implements FaceRepository {
        private final java.util.List<Face> store = new java.util.ArrayList<>();

        @Override
        public Face save(Face face) {
            store.add(face);
            return face;
        }

        @Override
        public List<Face> findByPersonId(UUID personId) {
            return store.stream().filter(f -> personId.equals(f.personId())).toList();
        }

        @Override
        public List<Face> findByEmbeddingNear(float[] embedding, double threshold, int limit) {
            return List.of();
        }

        @Override
        public void updatePersonId(UUID oldPersonId, UUID newPersonId) {
            for (int i = 0; i < store.size(); i++) {
                Face f = store.get(i);
                if (oldPersonId.equals(f.personId())) {
                    store.set(i, new Face(f.id(), newPersonId, f.embedding(), f.imageUrl(), f.confidence(), f.detectedAt()));
                }
            }
        }
    }
}
