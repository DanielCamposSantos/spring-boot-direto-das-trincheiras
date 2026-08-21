package io.github.danielcampossantos.brasilapi;

import lombok.Builder;

@Builder
public record CepInnerErrors(String name, String message, String service) {

}
