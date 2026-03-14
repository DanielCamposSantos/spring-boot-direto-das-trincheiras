package io.github.danielcampossantos.anime;

import lombok.Builder;

@Builder
public record AnimePostResponse(
        Long id,
        String name) {
}
