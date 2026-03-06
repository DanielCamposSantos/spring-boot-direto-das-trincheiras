package io.github.danielcampossantos.userservice.request;

public record UserPutRequest(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
