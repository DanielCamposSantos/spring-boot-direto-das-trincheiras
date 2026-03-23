package io.github.danielcampossantos.userprofile;

public record UserProfileUserGetResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
