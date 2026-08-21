package io.github.danielcampossantos.commons;

import io.github.danielcampossantos.brasil_api.CepErrorResponse;
import io.github.danielcampossantos.brasil_api.CepGetResponse;
import io.github.danielcampossantos.brasil_api.CepInnerErrors;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CepUtils {

    public CepGetResponse newCepGetResponse() {
        return CepGetResponse.builder()
                .cep("00000000")
                .state("SP")
                .city("São Paulo")
                .neighborhood("Centro")
                .street("Rua das Flores")
                .service("open-cep")
                .build();
    }

    public CepErrorResponse newCepErrorResponse() {
        var error = CepInnerErrors.builder()
                .name("ServiceError")
                .message("CEP INVÁLIDO")
                .service("correios")
                .build();


        return CepErrorResponse.builder()
                .name("CepPromiseError")
                .message("Todos os serviços de CEP retornaram erro.")
                .type("service_error")
                .errors(List.of(error))
                .build();

    }

}
