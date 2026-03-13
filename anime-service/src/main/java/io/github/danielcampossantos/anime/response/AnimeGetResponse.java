package io.github.danielcampossantos.anime.response;

import lombok.Builder;

@Builder
public record AnimeGetResponse(
        Long id,
        String name) {
}
