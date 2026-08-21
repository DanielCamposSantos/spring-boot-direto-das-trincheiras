package io.github.danielcampossantos.brasil_api;

import lombok.Builder;

@Builder
public record CepInnerErrors(String name, String message, String service) {
}
