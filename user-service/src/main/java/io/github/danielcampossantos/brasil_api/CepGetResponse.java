package io.github.danielcampossantos.brasil_api;

import lombok.Builder;

@Builder
public record CepGetResponse(
        String cep,
        String state,
        String city,
        String neighborhood,
        String street,
        String service) {
}
