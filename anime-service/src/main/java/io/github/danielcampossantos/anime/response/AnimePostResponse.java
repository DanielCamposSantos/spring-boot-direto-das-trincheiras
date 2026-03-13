package io.github.danielcampossantos.anime.response;

import lombok.Builder;

@Builder
public record AnimePostResponse(
        Long id,
        String name) {
}
