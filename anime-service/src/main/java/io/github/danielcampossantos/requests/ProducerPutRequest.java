package io.github.danielcampossantos.requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProducerPutRequest(
        @NotNull(message = "The field 'id' is required")
        Long id,
        @NotBlank(message = "The field 'name' is required")
        String name) {
}
