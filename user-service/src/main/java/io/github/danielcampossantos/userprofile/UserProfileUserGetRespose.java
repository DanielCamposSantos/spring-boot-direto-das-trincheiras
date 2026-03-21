package io.github.danielcampossantos.userprofile;

public record UserProfileUserGetRespose(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
