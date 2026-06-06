package dev.giro.vision.api;

import dev.giro.vision.face.domain.Face;
import dev.giro.vision.face.domain.FaceService;
import dev.giro.vision.vehicle.domain.VehicleService;
import dev.giro.vision.vehicle.port.PlateRecognitionPort.PlateResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/detect")
@Tag(name = "Detection", description = "Face and plate detection endpoints")
public class DetectionController {

    private final FaceService faceService;
    private final VehicleService vehicleService;

    public DetectionController(FaceService faceService, VehicleService vehicleService) {
        this.faceService = faceService;
        this.vehicleService = vehicleService;
    }

    @PostMapping(value = "/face", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Detect faces in an image")
    public List<FaceDetectionResponse> detectFaces(@RequestParam("image") MultipartFile image) throws IOException {
        List<Face> faces = faceService.detect(image.getBytes());
        return faces.stream()
                .map(f -> new FaceDetectionResponse(f.confidence()))
                .toList();
    }

    @PostMapping(value = "/plate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Read license plate from an image")
    public PlateDetectionResponse readPlate(@RequestParam("image") MultipartFile image) throws IOException {
        PlateResult result = vehicleService.readPlate(image.getBytes());
        return new PlateDetectionResponse(result.plate(), result.confidence());
    }

    public record FaceDetectionResponse(double confidence) {}

    public record PlateDetectionResponse(String plate, double confidence) {}
}
