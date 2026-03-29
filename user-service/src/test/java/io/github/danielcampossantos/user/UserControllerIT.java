package io.github.danielcampossantos.user;

import io.github.danielcampossantos.commons.FileUtils;
import io.github.danielcampossantos.config.IntegrationTestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerIT extends IntegrationTestConfig {
    private static final String URL = "/v1/users";

    @Autowired
    private FileUtils fileUtils;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUrl() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }


    @Test
    @DisplayName("GET /v1/users returns list with all users when successful")
    @Order(1)
    @Sql(value = "/sql/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnsListWithAllUsers_WhenNameIsNull() {
        var response = fileUtils.readResourceFile("user/get-user-null-name-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalToIgnoringCase(response))
                .log().all();

    }

    @Test
    @DisplayName("GET /v1/users?name=Cezar returns list user found by name when successful")
    @Order(2)
    @Sql(value = "/sql/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnsListWithUserFoundByName_WhenSuccessful() {
        var response = fileUtils.readResourceFile("user/get-user-cezar-name-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .param("name", "Cezar")
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalToIgnoringCase(response))
                .log().all();
    }


    @Test
    @DisplayName("GET /v1/users?name=x returns empty list when user not found")
    @Order(3)
    @Sql(value = "/sql/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnsEmptyList_WhenUserNotFound() {
        var response = fileUtils.readResourceFile("user/get-user-x-name-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .param("name", "x")
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalToIgnoringCase(response))
                .log().all();

    }


    @Test
    @DisplayName("GET /v1/users/1 returns user by id when successful")
    @Order(4)
    @Sql(value = "/sql/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_ReturnsUserById_WhenSuccessful() {
        var response = fileUtils.readResourceFile("user/get-user-by-id-200.json");
        var id = 1L;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL + "/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalToIgnoringCase(response))
                .log().all();
    }


    @Test
    @DisplayName("GET /v1/users/99 throws BadRequestException")
    @Order(5)
    void findById_ThrowsBadRequestException_WhenUserNotFoundById() {
        var response = fileUtils.readResourceFile("user/get-user-by-id-400.json");
        var id = 99L;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL + "/{id}", id)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(Matchers.equalToIgnoringCase(response))
                .log().all();
    }


    @Test
    @DisplayName("POST /v1/users creates user when successful")
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(6)
    void save_CreatesUser_WhenSuccessful() {
        var request = fileUtils.readResourceFile("user/post-request-user-200.json");
        var expectedResponse = fileUtils.readResourceFile("user/post-response-user-201.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().response().asString();

        JsonAssertions.assertThatJson(response)
                .node("id")
                .isNumber()
                .isPositive();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);

    }

    @Test
    @DisplayName("DELETE /v1/users/1 removes user when successful")
    @Order(7)
    @Sql(value = "/sql/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_RemovesUser_WhenSuccessful() {
        var id = 1L;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete(URL + "/{id}", id)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();

    }


    @Test
    @DisplayName("DELETE /v1/users/99 throws BadRequestException")
    @Order(8)
    void delete_ThrowsBadRequestException_WhenUserNotFound() {
        var expectedResponse = fileUtils.readResourceFile("user/delete-user-by-id-400.json");

        var id = 99L;
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete(URL + "/{id}", id)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(Matchers.equalToIgnoringCase(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("PUT /v1/users updates user when successful")
    @Order(9)
    @Sql(value = "/sql/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_UpdatesUser_WhenSuccessful() {
        var request = fileUtils.readResourceFile("user/put-request-user-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();

    }

    @Test
    @DisplayName("PUT /v1/users throws BadRequestException")
    @Order(10)
    @Sql(value = "/sql/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_ThrowsBadRequestException_WhenUserNotFound() {
        var request = fileUtils.readResourceFile("user/put-request-user-404.json");
        var expectedResponse = fileUtils.readResourceFile("user/put-user-by-id-400.json");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(Matchers.equalToIgnoringCase(expectedResponse))
                .log().all();

    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST /v1/users returns bad request when fields are invalid")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String requestFile, String responseFile) {
        var request = fileUtils.readResourceFile("user/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("user/%s".formatted(responseFile));

        var response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().asString();

        JsonAssertions.assertThatJson(expectedResponse)
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(response);

    }

    private static Stream<Arguments> postUserBadRequestSource() {
        return Stream.of(
                Arguments.of("post-request-user-empty-field-400.json", "post-response-user-empty-field-400.json"),
                Arguments.of("post-request-user-blank-field-400.json", "post-response-user-blank-field-400.json"),
                Arguments.of("post-request-user-null-field-400.json", "post-response-user-null-field-400.json"),
                Arguments.of("post-request-user-invalid-email-400.json", "post-response-user-invalid-email-400.json")
        );
    }

    //
    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT /v1/users throws BadRequestException when fields are invalid")
    @Order(10)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String requestFile, String responseFile) {
        var request = fileUtils.readResourceFile("user/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("user/%s".formatted(responseFile));

        var response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().asString();

        JsonAssertions.assertThatJson(expectedResponse)
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(response);
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        return Stream.of(
                Arguments.of("put-request-user-empty-field-400.json", "put-response-user-empty-field-400.json"),
                Arguments.of("put-request-user-blank-field-400.json", "put-response-user-blank-field-400.json"),
                Arguments.of("put-request-user-null-field-400.json", "put-response-user-null-field-400.json"),
                Arguments.of("put-request-user-invalid-email-400.json", "put-response-user-invalid-email-400.json")
        );
    }


}