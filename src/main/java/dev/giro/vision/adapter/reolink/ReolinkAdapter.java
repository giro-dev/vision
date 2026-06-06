package dev.giro.vision.adapter.reolink;

import dev.giro.vision.camera.port.CameraPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
public class ReolinkAdapter implements CameraPort {

    private static final Logger log = LoggerFactory.getLogger(ReolinkAdapter.class);

    @Override
    public byte[] captureSnapshot(String host, String user, String password) {
        try {
            String url = String.format("http://%s/cgi-bin/api.cgi?cmd=Snap&channel=0&user=%s&password=%s",
                    host, user, password);

            return WebClient.create()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .timeout(Duration.ofSeconds(10))
                    .block();
        } catch (Exception e) {
            log.error("Failed to capture snapshot from {}: {}", host, e.getMessage());
            return new byte[0];
        }
    }

    @Override
    public boolean isReachable(String host) {
        try {
            WebClient.create()
                    .get()
                    .uri("http://" + host)
                    .retrieve()
                    .toBodilessEntity()
                    .timeout(Duration.ofSeconds(5))
                    .block();
            return true;
        } catch (Exception e) {
            log.debug("Camera at {} is not reachable: {}", host, e.getMessage());
            return false;
        }
    }
}
