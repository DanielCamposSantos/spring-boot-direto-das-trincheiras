package io.github.danielcampossantos.response;

import lombok.Builder;

@Builder
public record ProducerPostResponse(
        Long id,
        String name) {
}
