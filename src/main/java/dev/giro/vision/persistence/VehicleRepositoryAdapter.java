package dev.giro.vision.persistence;

import dev.giro.vision.vehicle.domain.Vehicle;
import dev.giro.vision.vehicle.port.VehicleRepository;
import dev.giro.vision.persistence.entity.VehicleEntity;
import dev.giro.vision.persistence.jpa.VehicleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class VehicleRepositoryAdapter implements VehicleRepository {

    private final VehicleJpaRepository jpa;

    public VehicleRepositoryAdapter(VehicleJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        jpa.save(VehicleEntity.from(vehicle));
        return vehicle;
    }

    @Override
    public Optional<Vehicle> findByPlate(String plate) {
        return jpa.findByPlate(plate).map(VehicleEntity::toDomain);
    }

    @Override
    public List<Vehicle> findAll() {
        return jpa.findAll().stream().map(VehicleEntity::toDomain).toList();
    }
}
