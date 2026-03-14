package io.github.danielcampossantos.profile;

public record ProfileGetResponse(
        Long id,
        String name,
        String description
) {
}
