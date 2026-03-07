package io.github.danielcampossantos.userservice.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserPutRequest(
        @NotNull(message = "The field 'id' is can not be null")
        Long id,
        @NotBlank(message = "The field 'firstName' is required")
        String firstName,
        @NotBlank(message = "The field 'lastName' is required")
        String lastName,
        @NotBlank(message = "The field 'email' is required")
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "The e-mail is not valid")
        String email
) {
}
