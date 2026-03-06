package io.github.danielcampossantos.userservice.request;

public record UserPostRequest(
        String firstName,
        String lastName,
        String email
) {
}
