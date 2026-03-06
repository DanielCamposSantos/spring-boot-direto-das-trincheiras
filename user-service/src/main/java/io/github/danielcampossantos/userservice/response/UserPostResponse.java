package io.github.danielcampossantos.userservice.response;

public record UserPostResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
