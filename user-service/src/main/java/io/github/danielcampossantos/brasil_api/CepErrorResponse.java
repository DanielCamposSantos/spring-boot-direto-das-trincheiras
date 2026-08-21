package io.github.danielcampossantos.brasil_api;

import lombok.Builder;

import java.util.List;

@Builder
public record CepErrorResponse(String name, String message, String type, List<CepInnerErrors> errors) {
}

