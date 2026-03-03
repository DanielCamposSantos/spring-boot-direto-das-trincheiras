package io.github.danielcampossantos;

import io.github.danielcampossantos.config.ConnectionConfigurationPropreties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ConnectionConfigurationPropreties.class})

public class AnimeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimeServiceApplication.class, args);
    }

}
