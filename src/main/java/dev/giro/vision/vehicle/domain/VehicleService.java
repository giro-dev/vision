package dev.giro.vision.vehicle.domain;

import dev.giro.vision.vehicle.port.PlateRecognitionPort;
import dev.giro.vision.vehicle.port.PlateRecognitionPort.PlateResult;
import dev.giro.vision.vehicle.port.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final PlateRecognitionPort plateRecognitionPort;
    private final VehicleRepository vehicleRepository;

    public VehicleService(PlateRecognitionPort plateRecognitionPort,
                          VehicleRepository vehicleRepository) {
        this.plateRecognitionPort = plateRecognitionPort;
        this.vehicleRepository = vehicleRepository;
    }

    public PlateResult readPlate(byte[] image) {
        return plateRecognitionPort.readPlate(image);
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
