package dev.giro.vision.face.domain;

import dev.giro.vision.face.port.FaceRecognitionPort;
import dev.giro.vision.face.port.FaceRepository;
import dev.giro.vision.face.port.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FaceService {

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

    public Person registerPerson(String name) {
        return personRepository.save(Person.create(name));
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
