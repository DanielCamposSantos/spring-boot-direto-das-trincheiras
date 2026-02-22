package io.github.danielcampossantos.requests;

import lombok.Builder;

@Builder
public record AnimePutRequest(
        Long id,
        String name) {
}
