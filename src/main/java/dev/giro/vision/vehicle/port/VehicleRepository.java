package dev.giro.vision.vehicle.port;

import dev.giro.vision.vehicle.domain.Vehicle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository {

    Vehicle save(Vehicle vehicle);

    Optional<Vehicle> findByPlate(String plate);

    List<Vehicle> findAll();
}
