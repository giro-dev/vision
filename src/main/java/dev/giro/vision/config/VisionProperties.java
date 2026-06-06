package dev.giro.vision.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vision")
public record VisionProperties(
        CodeProjectAi codeprojectAi,
        Reolink reolink
) {

    public record CodeProjectAi(String url) {}

    public record Reolink(String defaultHost, String defaultUser, String defaultPassword) {}
}
