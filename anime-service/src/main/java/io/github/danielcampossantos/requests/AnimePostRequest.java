package io.github.danielcampossantos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AnimePostRequest(
        @NotBlank(message = "The field 'name' is required")
        String name) {
}
