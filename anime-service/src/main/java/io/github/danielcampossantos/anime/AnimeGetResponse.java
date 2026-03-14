package io.github.danielcampossantos.anime;

import lombok.Builder;

@Builder
public record AnimeGetResponse(
        Long id,
        String name) {
}
