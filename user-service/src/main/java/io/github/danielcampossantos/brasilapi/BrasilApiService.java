package io.github.danielcampossantos.brasilapi;


import io.github.danielcampossantos.config.BrasilApiConfigurationProperties;
import io.github.danielcampossantos.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class BrasilApiService {

  @Qualifier(value = "brasilApiClient")
  private final RestClient.Builder brasilApiClient;
  private final BrasilApiConfigurationProperties brasilApiConfigurationProperties;
  private final ObjectMapper mapper;

  public CepGetResponse findCep(String cep) {
    return brasilApiClient.build().get().uri(brasilApiConfigurationProperties.cepUri(), cep).retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
          var body = new String(response.getBody().readAllBytes());
          throw new BadRequestException(body);
        })
        .body(CepGetResponse.class);

  }
}
