package io.github.danielcampossantos.commons;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import static io.github.danielcampossantos.commons.Constants.*;

@TestConfiguration
@Lazy
public class RestAssuredConfig {
    @LocalServerPort
    private int port;

    @Bean(name = "requestSpecificationRegularUser")
    public RequestSpecification requestSpecificationRegularUser() {
        return RestAssured.given()
                .baseUri(BASE_URL + port)
                .auth().preemptive().basic(REGULAR_USER, PASSWORD);
    }
}
