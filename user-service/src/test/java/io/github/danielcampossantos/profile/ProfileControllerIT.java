package io.github.danielcampossantos.profile;

import io.github.danielcampossantos.commons.FileUtils;
import io.github.danielcampossantos.config.IntegrationTestConfig;
import lombok.SneakyThrows;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestRestTemplate
class ProfileControllerIT extends IntegrationTestConfig {
    private static final String URL = "/profiles";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FileUtils fileUtils;

    @Test
    @DisplayName("GET /profiles returns list with all profiles when argument is null")
    @Sql(value = "/sql/init_two_profiles.sql")
    @Order(1)
    void findAll_ReturnsListWithAllProfiles_WhenArgumentIsNull() {
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>() {};
        var responseEntity = testRestTemplate.exchange(URL, GET, null, typeReference);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Assertions.assertThat(responseEntity.getBody()).isNotNull().doesNotContainNull();

        responseEntity.getBody()
                .forEach(response -> Assertions.assertThat(response).hasNoNullFieldsOrProperties());

    }


    @Test
    @Order(2)
    @DisplayName("GET /profiles?name=admin returns profile found by name when successful")
    void findByName_ReturnsProfileFoundByName_WhenSuccessful() {

        var url = UriComponentsBuilder.fromPath(URL).queryParam("name", "admin").build().toUriString();

        var responseEntity = testRestTemplate.exchange(url, GET, null, ProfileGetResponse.class);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        Assertions.assertThat(responseEntity.getBody())
                .isNotNull();
    }

    @Test
    @Order(3)
    @DisplayName("GET /profiles?name=x throws BadRequestException when profile is not found")
    void findAll_ThrowsBadRequestException_WhenProfileNotFound() {

        var url = UriComponentsBuilder.fromPath(URL).queryParam("name", "x").build().toUriString();

        var responseEntity = testRestTemplate.exchange(url, GET, null, ProfileGetResponse.class);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @Order(5)
    @DisplayName("GET /profiles/1 returns profile found by id when when successful")
    void findById_ReturnsProfileFoundById_WhenSuccessful() {
        var id = 1L;

        var responseEntity = testRestTemplate.exchange(URL + "/{id}", GET, null, ProfileGetResponse.class, id);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        Assertions.assertThat(responseEntity.getBody())
                .isNotNull();
    }


    @Test
    @Order(6)
    @DisplayName("GET /profiles/99 throws BadRequestException when profile not found")
    @SneakyThrows
    void findById_ThrowsBadRequestException_WhenProfileNotFound() {
        var id = 99L;

        var responseEntity = testRestTemplate.exchange(URL + "/{id}", GET, null, ProfileGetResponse.class, id);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        Assertions.assertThat(responseEntity.getBody())
                .isNotNull();

    }

    @Test
    @Order(7)
    @DisplayName("POST /profiles returns saved profile when successful")
    @SneakyThrows
    void save_ReturnsSavedProfile_WhenSuccessful() {
        var request = fileUtils.readResourceFile("profile/post-request-profile-200.json");

        var httpEntity = buildHttpEntity(request);

        var responseEntity = testRestTemplate.exchange(URL, POST, httpEntity, ProfilePostRequest.class);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(responseEntity.getBody())
                .isNotNull();

    }

    private static HttpEntity<String> buildHttpEntity(String request) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(request, headers);
    }


    @ParameterizedTest
    @MethodSource("getProfilesBadRequestSources")
    @Order(8)
    @DisplayName("POST /profiles throws BadRequestException when invalid fields")
    @SneakyThrows
    void save_ThrowsBadRequestException_WhenInvalidFields(String requestFile, String responseFile) {
        var request = fileUtils.readResourceFile("profile/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("profile/%s".formatted(responseFile));

        var httpEntity = buildHttpEntity(request);

        var responseEntity = testRestTemplate.exchange(URL, POST, httpEntity, String.class);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        JsonAssertions.assertThatJson(responseEntity.getBody())
                .when(Option.IGNORING_ARRAY_ORDER)
                .whenIgnoringPaths("timestamp")
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
