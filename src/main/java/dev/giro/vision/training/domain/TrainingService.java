package dev.giro.vision.training.domain;

import dev.giro.vision.face.domain.Face;
import dev.giro.vision.face.port.FaceRecognitionPort;
import dev.giro.vision.face.port.FaceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrainingService {

    private final FaceRecognitionPort recognitionPort;
    private final FaceRepository faceRepository;

    public TrainingService(FaceRecognitionPort recognitionPort, FaceRepository faceRepository) {
        this.recognitionPort = recognitionPort;
        this.faceRepository = faceRepository;
    }

    public Face trainFace(UUID personId, byte[] faceImage, String imageUrl) {
        float[] embedding = recognitionPort.generateEmbedding(faceImage);
        Face face = Face.detected(personId, embedding, imageUrl, 1.0);
        return faceRepository.save(face);
    }
}
