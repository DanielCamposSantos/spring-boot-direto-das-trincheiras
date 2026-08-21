package io.github.danielcampossantos.config;

import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@EnableWireMock(
        @ConfigureWireMock(
                port = 0,
                filesUnderClasspath = "wiremock/brasil-api/cep"
        )

)
public @interface DefaultWiremockConfiguration {


}
