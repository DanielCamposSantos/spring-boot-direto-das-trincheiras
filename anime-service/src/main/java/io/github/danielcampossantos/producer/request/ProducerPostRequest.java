package io.github.danielcampossantos.producer.request;


import jakarta.validation.constraints.NotBlank;

public record ProducerPostRequest(
        @NotBlank(message = "The field 'name' is required")
        String name
) {
}
