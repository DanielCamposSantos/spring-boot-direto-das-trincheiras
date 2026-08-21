package io.github.danielcampossantos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "brasil-api")
public record BrasilApiConfigurationProperties(String baseUrl, String cepUri) {


}
