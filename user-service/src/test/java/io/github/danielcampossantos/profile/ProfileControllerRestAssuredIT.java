package io.github.danielcampossantos.profile;

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
class ProfileControllerRestAssuredIT extends IntegrationTestConfig {
    private static final String URL = "/profiles";

    @LocalServerPort
    private int port;

    @Autowired
    private FileUtils fileUtils;


    @BeforeEach
    void setUrl() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }


    @Test
    @Order(1)
    @DisplayName("GET /profiles returns list with all profiles when argument is null")
    @Sql(value = "/sql/init_two_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnsListWithAllProfiles_WhenArgumentIsNull() {
        var response = fileUtils.readResourceFile("profile/get-profile-200.json");

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
    @Order(2)
    @DisplayName("GET /profiles?name=Admin returns profile found by name when successful")
    @Sql(value = "/sql/init_two_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByName_ReturnsProfileFoundByName_WhenSuccessful() {
        var response = fileUtils.readResourceFile("profile/get-profile-tester-name-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .param("name", "Admin")
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalToIgnoringCase(response))
                .log().all();
    }

    @Test
    @Order(3)
    @DisplayName("GET /profiles?name=x throws BadRequestException when profile is not found")
    @Sql(value = "/sql/init_two_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ThrowsBadRequestException_WhenProfileNotFound() {
        var response = fileUtils.readResourceFile("profile/get-profile-x-name-400.json");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .param("name", "x")
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(Matchers.equalToIgnoringCase(response))
                .log().all();
    }

    @Test
    @Order(5)
    @DisplayName("GET /profiles/1 returns profile found by id when when successful")
    @Sql(value = "/sql/init_two_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_ReturnsProfileFoundById_WhenSuccessful() {
        var response = fileUtils.readResourceFile("profile/get-profile-by-id-200.json");

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
    @Order(6)
    @DisplayName("GET /profiles/99 throws BadRequestException when profile not found")
    @Sql(value = "/sql/init_two_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_ThrowsBadRequestException_WhenProfileNotFound() {
        var response = fileUtils.readResourceFile("profile/get-profile-by-id-400.json");

        var id = 99L;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get(URL + "/{id}", id)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(Matchers.equalToIgnoringCase(response))
                .log().all();

    }

    @Test
    @Order(7)
    @DisplayName("POST /profiles returns saved profile when successful")
    void save_ReturnsSavedProfile_WhenSuccessful() {
        var request = fileUtils.readResourceFile("profile/post-request-profile-200.json");
        var expectedResponse = fileUtils.readResourceFile("profile/post-response-profile-201.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
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

    @ParameterizedTest
    @MethodSource("getProfilesBadRequestSources")
    @Order(8)
    @DisplayName("POST /profiles throws BadRequestException when invalid fields")
    void save_ThrowsBadRequestException_WhenInvalidFields(String requestFile, String responseFile) {
        var request = fileUtils.readResourceFile("profile/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("profile/%s".formatted(responseFile));


        var response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().asString();

        JsonAssertions.assertThatJson(response)
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);

    }

    private static Stream<Arguments> getProfilesBadRequestSources() {
        return Stream.of(
                Arguments.of("post-request-profile-blank-field-400.json", "post-response-profile-blank-field-400.json"),
                Arguments.of("post-request-profile-empty-field-400.json", "post-response-profile-empty-field-400.json"),
                Arguments.of("post-request-profile-null-field-400.json", "post-response-profile-null-field-400.json")
        );
    }


}
