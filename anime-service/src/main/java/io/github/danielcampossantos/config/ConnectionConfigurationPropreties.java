package io.github.danielcampossantos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "database")
public record ConnectionConfigurationPropreties(String url, String username, String password) {

}
