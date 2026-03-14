package io.github.danielcampossantos.profile;

import jakarta.validation.constraints.NotBlank;

public record ProfilePostRequest(
        @NotBlank(message = "The field 'name' is required")
        String name,
        @NotBlank(message = "The field 'description' is required")
        String description
) {
}
