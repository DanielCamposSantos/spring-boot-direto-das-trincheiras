package io.github.danielcampossantos.producer.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProducerGetResponse(
        Long id,
        String name,
        LocalDateTime createdAt
) {
}
