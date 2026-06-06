package dev.giro.vision.vehicle.port;

/**
 * Abstraction over license plate recognition engine.
 */
public interface PlateRecognitionPort {

    PlateResult readPlate(byte[] image);

    record PlateResult(String plate, double confidence, int x, int y, int width, int height) {}
}
