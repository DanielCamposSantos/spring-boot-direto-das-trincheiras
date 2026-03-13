package io.github.danielcampossantos.anime.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AnimePostRequest(
        @NotBlank(message = "The field 'name' is required")
        String name) {
}
