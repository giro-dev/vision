package dev.giro.vision.camera.port;

/**
 * Abstraction over camera providers / feeds.
 */
public interface CameraPort {

    byte[] captureSnapshot(String host, String user, String password);

    boolean isReachable(String host);
}
