package io.github.danielcampossantos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.parameters.Parameter; // IMPORTANTE: Importar o Parameter do pacote .models
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(info = @Info(title = "API de animes", version = "v1"))
public class OpenApiConfig {

    @Bean
    public OperationCustomizer addGlobalHeader() {
        return (operation, handlerMethod) -> {

            Parameter xApiVersionHeader = new Parameter()
                    .in("header")
                    .name("X-API-Version")
                    .description("Versão da api")
                    .required(false)
                    .example("1");

            operation.addParametersItem(xApiVersionHeader);

            return operation;
        };
    }
}
