package io.github.danielcampossantos.brasil_api;

import io.github.danielcampossantos.commons.CepUtils;
import io.github.danielcampossantos.config.BrasilApiConfigurationProperties;
import io.github.danielcampossantos.config.RestClientConfiguration;
import io.github.danielcampossantos.exception.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@RestClientTest({
        BrasilApiService.class,
        BrasilApiConfigurationProperties.class,
        ObjectMapper.class,
        MockRestServiceServer.class,
        CepUtils.class,
        RestClientConfiguration.class
})
class BrasilApiServiceTest {
    @Autowired
    private BrasilApiService service;

    @Autowired
    @Qualifier("brasilApiClient")
    private RestClient.Builder brasilApiClient;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private BrasilApiConfigurationProperties properties;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CepUtils cepUtils;

    @AfterEach
    void reset() {
        server.reset();
    }

    @Test
    @DisplayName("findCep returns CepGetResponse when successful")
    void findCep_ReturnsCepGetResponse_WhenSuccessful() {
        server = MockRestServiceServer.bindTo(brasilApiClient).build();

        var cep = "0000000";
        var cepGetResponse = cepUtils.newCepGetResponse();
        var jsonResponse = mapper.writeValueAsString(cepGetResponse);

        var requestTo = MockRestRequestMatchers.requestToUriTemplate(
                properties.baseUrl() + properties.cepUri(),
                cep
        );
        var withSuccess = MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON);

        server.expect(requestTo).andRespond(withSuccess);

        Assertions.assertThat(service.findCep(cep))
                .isNotNull()
                .isEqualTo(cepGetResponse);

    }

    @Test
    @DisplayName("findCep throws BadRequestException when invalid cpf")
    void findCep_ThrowsBadRequestException_WhenInvalidCpf() {
        server = MockRestServiceServer.bindTo(brasilApiClient).build();

        var cep = "invalidCpf";
        var cepErrorResponse = cepUtils.newCepErrorResponse();
        var jsonResponse = mapper.writeValueAsString(cepErrorResponse);

        var errorMessage = """
                400 BAD_REQUEST "{"name":"CepPromiseError","message":"Todos os serviços de CEP retornaram erro.","type":"service_error","errors":[{"name":"ServiceError","message":"CEP INVÁLIDO","service":"correios"}]}"
                """.trim();

        var requestTo = MockRestRequestMatchers.requestToUriTemplate(
                properties.baseUrl() + properties.cepUri(),
                cep
        );
        var withError = MockRestResponseCreators.withResourceNotFound().body(jsonResponse);

        server.expect(requestTo).andRespond(withError);

        Assertions.assertThatException().isThrownBy(() -> service.findCep(cep))
                .withMessage(errorMessage)
                .isInstanceOf(BadRequestException.class);

    }
}