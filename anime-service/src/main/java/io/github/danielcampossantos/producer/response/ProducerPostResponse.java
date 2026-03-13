package io.github.danielcampossantos.producer.response;

import lombok.Builder;

@Builder
public record ProducerPostResponse(
        Long id,
        String name) {
}
