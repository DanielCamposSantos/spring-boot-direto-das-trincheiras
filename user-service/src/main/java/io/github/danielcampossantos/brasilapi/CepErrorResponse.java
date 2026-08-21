package io.github.danielcampossantos.brasilapi;

import java.util.List;
import lombok.Builder;

@Builder
public record CepErrorResponse(String name, String message, String type, List<CepInnerErrors> errors) {

}

