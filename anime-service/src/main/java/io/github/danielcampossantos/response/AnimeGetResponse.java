package io.github.danielcampossantos.response;

import lombok.Builder;

@Builder
public record AnimeGetResponse(
        Long id,
        String name) {
}
