package io.github.danielcampossantos.producer;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProducerGetResponse(
        Long id,
        String name,
        LocalDateTime createdAt
) {
}
