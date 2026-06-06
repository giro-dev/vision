package dev.giro.vision.face.domain;

import dev.giro.vision.face.port.FaceRecognitionPort;
import dev.giro.vision.face.port.FaceRepository;
import dev.giro.vision.face.port.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FaceService {

    private static final double SIMILARITY_THRESHOLD = 0.6;
    private static final int MAX_MATCHES = 5;

    private final FaceRecognitionPort recognitionPort;
    private final FaceRepository faceRepository;
    private final PersonRepository personRepository;

    public FaceService(FaceRecognitionPort recognitionPort,
                       FaceRepository faceRepository,
                       PersonRepository personRepository) {
        this.recognitionPort = recognitionPort;
        this.faceRepository = faceRepository;
        this.personRepository = personRepository;
    }

    public List<Face> detect(byte[] image) {
        return recognitionPort.detectFaces(image);
    }

    public float[] embed(byte[] faceImage) {
        return recognitionPort.generateEmbedding(faceImage);
    }

    public Optional<Person> recognize(byte[] faceImage) {
        float[] embedding = recognitionPort.generateEmbedding(faceImage);
        List<Face> matches = faceRepository.findByEmbeddingNear(embedding, SIMILARITY_THRESHOLD, MAX_MATCHES);
        if (matches.isEmpty()) {
            return Optional.empty();
        }
        UUID personId = matches.getFirst().personId();
        return personRepository.findById(personId);
    }

    public Person registerPerson(String name) {
        return personRepository.save(Person.create(name));
    }

    public Person updatePerson(UUID id, String name) {
        Person existing = getPerson(id);
        Person updated = new Person(existing.id(), name, existing.createdAt());
        return personRepository.save(updated);
    }

    public Person mergePersons(UUID targetId, UUID sourceId) {
        Person target = getPerson(targetId);
        getPerson(sourceId);
        faceRepository.updatePersonId(sourceId, targetId);
        personRepository.deleteById(sourceId);
        return target;
    }

    public List<Person> listPersons() {
        return personRepository.findAll();
    }

    public Person getPerson(UUID id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Person not found: " + id));
    }

    public void deletePerson(UUID id) {
        personRepository.deleteById(id);
    }

    public Face addFaceForPerson(UUID personId, byte[] faceImage, String imageUrl) {
        float[] embedding = recognitionPort.generateEmbedding(faceImage);
        Face face = Face.detected(personId, embedding, imageUrl, 1.0);
        return faceRepository.save(face);
    }

    public List<Face> getFacesForPerson(UUID personId) {
        return faceRepository.findByPersonId(personId);
    }
}
