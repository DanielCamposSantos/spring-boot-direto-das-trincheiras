package io.github.danielcampossantos.response;

import lombok.Builder;

@Builder
public record UserGetResponse(
        Long id,
        String firstName,
        String lastName,
        String email) {

}
