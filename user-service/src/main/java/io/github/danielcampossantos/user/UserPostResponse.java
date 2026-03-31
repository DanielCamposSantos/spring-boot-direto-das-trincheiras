package io.github.danielcampossantos.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserPostResponse(
        @Schema(description = "New saved user id. Must be unique", example = "99")
        Long id
) {
}
