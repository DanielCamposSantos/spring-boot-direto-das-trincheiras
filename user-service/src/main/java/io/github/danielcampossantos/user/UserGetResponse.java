package io.github.danielcampossantos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record UserGetResponse(
        @Schema(description = "User's id", example = "1")
        Long id,
        @Schema(description = "User's first name", example = "Paulo")
        String firstName,
        @Schema(description = "User's last name", example = "Alcantara")
        String lastName,
        @Schema(description = "User's email", example = "pauloalcantara@email.com")
        String email) {

}
