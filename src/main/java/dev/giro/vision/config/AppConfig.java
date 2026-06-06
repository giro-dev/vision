package dev.giro.vision.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(VisionProperties.class)
public class AppConfig {

    @Bean
    public WebClient codeProjectAiWebClient(VisionProperties props) {
        return WebClient.builder()
                .baseUrl(props.codeprojectAi().url())
                .build();
    }
}
