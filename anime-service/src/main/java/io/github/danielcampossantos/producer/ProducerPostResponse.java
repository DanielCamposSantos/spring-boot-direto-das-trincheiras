package io.github.danielcampossantos.producer;

import lombok.Builder;

@Builder
public record ProducerPostResponse(
        Long id,
        String name) {
}
