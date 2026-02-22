package io.github.danielcampossantos.response;

import lombok.Builder;

@Builder
public record AnimePostResponse(
        Long id,
        String name) {
}
