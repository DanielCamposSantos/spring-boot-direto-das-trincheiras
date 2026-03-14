package io.github.danielcampossantos.producer;


import jakarta.validation.constraints.NotBlank;

public record ProducerPostRequest(
        @NotBlank(message = "The field 'name' is required")
        String name
) {
}
