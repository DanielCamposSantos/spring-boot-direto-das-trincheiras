package io.github.danielcampossantos.requests;

import lombok.Builder;

@Builder
public record AnimePostRequest(
        String name) {
}
