package io.github.danielcampossantos.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class ConnectionBeanConfiguration {
    private final ConnectionConfigurationPropreties configurationPropreties;

    @Bean
    @Primary
    public Connection connectionMySql() {
        return new Connection(
                configurationPropreties.url(),
                configurationPropreties.username(),
                configurationPropreties.password());
    }

    @Bean(name = "connectionMongoDB")
    @Profile("mongo")
    public Connection connectionMongo() {
        return new Connection(
                configurationPropreties.url(),
                configurationPropreties.username(),
                configurationPropreties.password());
    }
}
