package dev.giro.vision.vehicle.port;

import java.util.List;

/**
 * Abstraction over vehicle detection engine (e.g. YOLOv8).
 */
public interface VehicleDetectionPort {

    List<DetectedVehicle> detectVehicles(byte[] image);

    record DetectedVehicle(
            int x, int y, int width, int height,
            String type, double confidence,
            byte[] croppedImage
    ) {}
}
