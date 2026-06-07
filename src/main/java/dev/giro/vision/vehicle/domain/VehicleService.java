package dev.giro.vision.vehicle.domain;

import dev.giro.vision.vehicle.port.PlateRecognitionPort;
import dev.giro.vision.vehicle.port.PlateRecognitionPort.PlateResult;
import dev.giro.vision.vehicle.port.VehicleDetectionPort;
import dev.giro.vision.vehicle.port.VehicleDetectionPort.DetectedVehicle;
import dev.giro.vision.vehicle.port.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final PlateRecognitionPort plateRecognitionPort;
    private final VehicleDetectionPort vehicleDetectionPort;
    private final VehicleRepository vehicleRepository;

    public VehicleService(PlateRecognitionPort plateRecognitionPort,
                          VehicleDetectionPort vehicleDetectionPort,
                          VehicleRepository vehicleRepository) {
        this.plateRecognitionPort = plateRecognitionPort;
        this.vehicleDetectionPort = vehicleDetectionPort;
        this.vehicleRepository = vehicleRepository;
    }

    public PlateResult readPlate(byte[] image) {
        return plateRecognitionPort.readPlate(image);
    }

    public List<Vehicle> detectAndIdentify(byte[] image) {
        List<DetectedVehicle> detections = vehicleDetectionPort.detectVehicles(image);
        List<Vehicle> results = new ArrayList<>();
        for (DetectedVehicle detection : detections) {
            PlateResult plate = plateRecognitionPort.readPlate(detection.croppedImage());
            if (!plate.plate().isBlank()) {
                Vehicle vehicle = registerDetection(
                        plate.plate(), null, detection.type(), plate.confidence());
                results.add(vehicle);
            }
        }
        return results;
    }

    public Vehicle registerDetection(String plate, String color, String type, double confidence) {
        Optional<Vehicle> existing = vehicleRepository.findByPlate(plate);
        if (existing.isPresent()) {
            return existing.get();
        }
        return vehicleRepository.save(Vehicle.detected(plate, color, type, confidence));
    }

    public List<Vehicle> listAll() {
        return vehicleRepository.findAll();
    }
}
