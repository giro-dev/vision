package dev.giro.vision.face.port;

import dev.giro.vision.face.domain.Face;

import java.util.List;

/**
 * Abstraction over face detection / recognition engine.
 */
public interface FaceRecognitionPort {

    List<Face> detectFaces(byte[] image);

    float[] generateEmbedding(byte[] faceImage);

    double compareFaces(float[] embedding1, float[] embedding2);
}
