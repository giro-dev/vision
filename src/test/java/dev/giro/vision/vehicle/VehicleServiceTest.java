package dev.giro.vision.vehicle;

import dev.giro.vision.vehicle.domain.Vehicle;
import dev.giro.vision.vehicle.domain.VehicleService;
import dev.giro.vision.vehicle.port.PlateRecognitionPort;
import dev.giro.vision.vehicle.port.PlateRecognitionPort.PlateResult;
import dev.giro.vision.vehicle.port.VehicleDetectionPort;
import dev.giro.vision.vehicle.port.VehicleDetectionPort.DetectedVehicle;
import dev.giro.vision.vehicle.port.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleServiceTest {

    private VehicleService vehicleService;
    private InMemoryVehicleRepository vehicleRepo;

    @BeforeEach
    void setUp() {
        vehicleRepo = new InMemoryVehicleRepository();
        PlateRecognitionPort platePort = new StubPlateRecognitionPort();
        VehicleDetectionPort detectionPort = new StubVehicleDetectionPort();
        vehicleService = new VehicleService(platePort, detectionPort, vehicleRepo);
    }

    @Test
    void readPlateReturnsResult() {
        PlateResult result = vehicleService.readPlate(new byte[]{1, 2});
        assertThat(result.plate()).isEqualTo("ABC1234");
        assertThat(result.confidence()).isGreaterThan(0.9);
    }

    @Test
    void registerDetectionStoresVehicle() {
        Vehicle v = vehicleService.registerDetection("XYZ789", "red", "sedan", 0.95);
        assertThat(v.plate()).isEqualTo("XYZ789");
        assertThat(vehicleService.listAll()).hasSize(1);
    }

    @Test
    void duplicatePlateReturnsExisting() {
        vehicleService.registerDetection("DUP123", "blue", "suv", 0.9);
        Vehicle second = vehicleService.registerDetection("DUP123", "blue", "suv", 0.85);
        assertThat(vehicleService.listAll()).hasSize(1);
        assertThat(second.plate()).isEqualTo("DUP123");
    }

    @Test
    void detectAndIdentifyPipeline() {
        List<Vehicle> vehicles = vehicleService.detectAndIdentify(new byte[]{1, 2, 3});
        assertThat(vehicles).hasSize(1);
        assertThat(vehicles.getFirst().plate()).isEqualTo("ABC1234");
    }

    // ---- stubs ----

    static class StubPlateRecognitionPort implements PlateRecognitionPort {
        @Override
        public PlateResult readPlate(byte[] image) {
            return new PlateResult("ABC1234", 0.97, 10, 20, 100, 50);
        }
    }

    static class StubVehicleDetectionPort implements VehicleDetectionPort {
        @Override
        public List<DetectedVehicle> detectVehicles(byte[] image) {
            return List.of(new DetectedVehicle(10, 20, 200, 100, "car", 0.95, new byte[]{1}));
        }
    }

    static class InMemoryVehicleRepository implements VehicleRepository {
        private final List<Vehicle> store = new ArrayList<>();

        @Override
        public Vehicle save(Vehicle vehicle) {
            store.add(vehicle);
            return vehicle;
        }

        @Override
        public Optional<Vehicle> findByPlate(String plate) {
            return store.stream().filter(v -> plate.equals(v.plate())).findFirst();
        }

        @Override
        public List<Vehicle> findAll() {
            return List.copyOf(store);
        }
    }
}
