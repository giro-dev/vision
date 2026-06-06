package dev.giro.vision.adapter.codeprojectai;

import dev.giro.vision.face.domain.Face;
import dev.giro.vision.face.port.FaceRecognitionPort;
import dev.giro.vision.vehicle.port.PlateRecognitionPort;
import dev.giro.vision.vehicle.port.VehicleDetectionPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class CodeProjectAiAdapter implements FaceRecognitionPort, PlateRecognitionPort, VehicleDetectionPort {

    private static final Logger log = LoggerFactory.getLogger(CodeProjectAiAdapter.class);

    private final WebClient webClient;

    public CodeProjectAiAdapter(WebClient codeProjectAiWebClient) {
        this.webClient = codeProjectAiWebClient;
    }

    @Override
    public List<Face> detectFaces(byte[] image) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("image", new ByteArrayResource(image) {
                @Override public String getFilename() { return "image.jpg"; }
            }).contentType(MediaType.IMAGE_JPEG);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.post()
                    .uri("/v1/vision/face")
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !Boolean.TRUE.equals(response.get("success"))) {
                log.warn("Face detection returned no results");
                return List.of();
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> predictions = (List<Map<String, Object>>) response.get("predictions");
            if (predictions == null) return List.of();

            return predictions.stream()
                    .map(p -> {
                        double confidence = ((Number) p.getOrDefault("confidence", 0.0)).doubleValue();
                        return Face.detected(null, new float[0], null, confidence);
                    })
                    .toList();
        } catch (Exception e) {
            log.error("Face detection failed: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public float[] generateEmbedding(byte[] faceImage) {
        // CodeProject.AI face recognition endpoint returns embeddings when using /v1/vision/face/recognize
        log.info("Embedding generation requested (stub — requires model registration)");
        return new float[512];
    }

    @Override
    public double compareFaces(float[] embedding1, float[] embedding2) {
        if (embedding1.length != embedding2.length) return 0.0;
        double dot = 0, norm1 = 0, norm2 = 0;
        for (int i = 0; i < embedding1.length; i++) {
            dot += embedding1[i] * embedding2[i];
            norm1 += embedding1[i] * embedding1[i];
            norm2 += embedding2[i] * embedding2[i];
        }
        double denom = Math.sqrt(norm1) * Math.sqrt(norm2);
        return denom == 0 ? 0.0 : dot / denom;
    }

    @Override
    public PlateResult readPlate(byte[] image) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("image", new ByteArrayResource(image) {
                @Override public String getFilename() { return "image.jpg"; }
            }).contentType(MediaType.IMAGE_JPEG);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.post()
                    .uri("/v1/image/alpr")
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !Boolean.TRUE.equals(response.get("success"))) {
                log.warn("Plate recognition returned no results");
                return new PlateResult("", 0.0, 0, 0, 0, 0);
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> predictions = (List<Map<String, Object>>) response.get("predictions");
            if (predictions == null || predictions.isEmpty()) {
                return new PlateResult("", 0.0, 0, 0, 0, 0);
            }

            Map<String, Object> first = predictions.getFirst();
            String plate = (String) first.getOrDefault("plate", "");
            double confidence = ((Number) first.getOrDefault("confidence", 0.0)).doubleValue();
            int x = ((Number) first.getOrDefault("x_min", 0)).intValue();
            int y = ((Number) first.getOrDefault("y_min", 0)).intValue();
            int w = ((Number) first.getOrDefault("x_max", 0)).intValue() - x;
            int h = ((Number) first.getOrDefault("y_max", 0)).intValue() - y;

            return new PlateResult(plate, confidence, x, y, w, h);
        } catch (Exception e) {
            log.error("Plate recognition failed: {}", e.getMessage());
            return new PlateResult("", 0.0, 0, 0, 0, 0);
        }
    }

    @Override
    public List<DetectedVehicle> detectVehicles(byte[] image) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("image", new ByteArrayResource(image) {
                @Override public String getFilename() { return "image.jpg"; }
            }).contentType(MediaType.IMAGE_JPEG);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.post()
                    .uri("/v1/vision/detection")
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !Boolean.TRUE.equals(response.get("success"))) {
                log.warn("Vehicle detection returned no results");
                return List.of();
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> predictions = (List<Map<String, Object>>) response.get("predictions");
            if (predictions == null) return List.of();

            return predictions.stream()
                    .filter(p -> {
                        String label = (String) p.getOrDefault("label", "");
                        return "car".equals(label) || "truck".equals(label)
                                || "bus".equals(label) || "motorcycle".equals(label);
                    })
                    .map(p -> {
                        double confidence = ((Number) p.getOrDefault("confidence", 0.0)).doubleValue();
                        int x = ((Number) p.getOrDefault("x_min", 0)).intValue();
                        int y = ((Number) p.getOrDefault("y_min", 0)).intValue();
                        int w = ((Number) p.getOrDefault("x_max", 0)).intValue() - x;
                        int h = ((Number) p.getOrDefault("y_max", 0)).intValue() - y;
                        String type = (String) p.getOrDefault("label", "unknown");
                        return new DetectedVehicle(x, y, w, h, type, confidence, new byte[0]);
                    })
                    .toList();
        } catch (Exception e) {
            log.error("Vehicle detection failed: {}", e.getMessage());
            return List.of();
        }
    }
}
