package io.github.danielcampossantos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserPutRequest(
        @NotNull(message = "The field 'id' is can not be null")
        @Schema(description = "User's id", example = "1")
        Long id,
        @NotBlank(message = "The field 'firstName' is required")
        @Schema(description = "User's first name", example = "Paulo")
        String firstName,
        @NotBlank(message = "The field 'lastName' is required")
        @Schema(description = "User's last name", example = "Alcantara")
        String lastName,
        @NotBlank(message = "The field 'email' is required")
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "The e-mail is not valid")
        @Schema(description = "User's email", example = "pauloalcantara@email.com")
        String email,
        String password
) {
}
