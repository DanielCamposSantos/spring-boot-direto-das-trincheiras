package io.github.danielcampossantos.brasil_api;

import io.github.danielcampossantos.commons.FileUtils;
import io.github.danielcampossantos.config.DefaultWiremockConfiguration;
import io.github.danielcampossantos.config.IntegrationTestConfig;
import io.github.danielcampossantos.config.RestAssuredConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfig.class)
@Sql(value = "/sql/init_one_login_regular_user.sql")
@Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DefaultWiremockConfiguration
class BrasilApiControllerIT extends IntegrationTestConfig {
    private static final String BASE_URL = "/v1/brasil-api/cep";

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    @Qualifier("requestSpecificationRegularUser")
    private RequestSpecification requestSpecification;


    @BeforeEach
    void setup() {
        RestAssured.requestSpecification = requestSpecification;
    }

    @Test
    @DisplayName("findCep returns CepGetResponse when successful")
    void findCep_ReturnsCepGetResponse_WhenSuccessful() {
        var cep = "89010025";
        var expectedResponse = fileUtils.readResourceFile("brasil-api/cep/expected/get-cep-response-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(BASE_URL + "/{cep}", cep)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();

    }


    @Test
    @DisplayName("findCep throws BadRequestException when invalid cpf")
    void findCep_ThrowsBadRequestException_WhenInvalidCpf() {
        var cep = "00000000";
        var expectedResponse = fileUtils.readResourceFile("brasil-api/cep/expected/get-cep-response-400.json");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(BASE_URL + "/{cep}", cep)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();

    }

}
