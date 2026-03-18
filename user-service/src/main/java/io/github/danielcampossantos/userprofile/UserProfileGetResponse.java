package io.github.danielcampossantos.userprofile;

public record UserProfileGetResponse(
        Long id,
        UserProfileUser user,
        UserProfileProfile profile
) {
    record UserProfileUser(Long id, String firstName) {

    }

    record UserProfileProfile(Long id, String name) {

    }
}
