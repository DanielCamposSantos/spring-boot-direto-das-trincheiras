package io.github.danielcampossantos.controller;

import io.github.danielcampossantos.commons.AnimeUtils;
import io.github.danielcampossantos.commons.FileUtils;
import io.github.danielcampossantos.domain.Anime;
import io.github.danielcampossantos.repository.AnimeRepository;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = AnimeController.class)
@ComponentScan(basePackages = "io.github.danielcampossantos")
class AnimeControllerTest {
    private static final String URL = "/animes";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnimeRepository repository;


    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private AnimeUtils animeUtils;

    private List<Anime> animesList;

    @BeforeEach
    void init() {
        animesList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("GET /animes returns list with all animes when argument is null")
    @Order(1)
    @SneakyThrows
    void findAll_ReturnsListWithAnimes_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(animesList);

        var response = fileUtils.readResourceFiles("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET /animes?name=Mashle returns found anime in list when name exists")
    @Order(2)
    @SneakyThrows
    void findAll_ReturnsFoundAnimeInList_WhenNameExists() {
        var anime = animesList.getFirst();
        var name = anime.getName();
        BDDMockito.when(repository.findByNameIgnoreCase(name)).thenReturn(Collections.singletonList(anime));

        var response = fileUtils.readResourceFiles("anime/get-anime-mashle-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET /animes?name=x returns empty list of animes when argument is not found")
    @Order(3)
    @SneakyThrows
    void findAll_ReturnsEmptyListOfAnimes_WhenArgumentIsNotFound() {
        BDDMockito.when(repository.findAll()).thenReturn(animesList);

        var response = fileUtils.readResourceFiles("anime/get-anime-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET /animes/1 returns anime by id")
    @Order(4)
    @SneakyThrows
    void findById_ReturnsAnimeById_WhenSuccssesful() {
        var anime = animesList.getFirst();
        var id = anime.getId();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(anime));

        var response = fileUtils.readResourceFiles("anime/get-anime-by-id-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET /animes/99 throws BadRequestException 400 when anime is not found")
    @Order(5)
    @SneakyThrows
    void findById_ThrowsBadRequestException_WhenAnimeIsNotFound() {
        var id = 99L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        var response = fileUtils.readResourceFiles("anime/get-anime-by-id-400.json");


        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }


    @Test
    @DisplayName("POST /animes creates a anime")
    @Order(6)
    @SneakyThrows
    void save_CreatesAnime_WhenSuccessful() {
        var request = fileUtils.readResourceFiles("anime/post-request-anime-200.json");
        var response = fileUtils.readResourceFiles("anime/post-response-anime-201.json");

        var animeToSave = animeUtils.newAnimeToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToSave);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE /animes/1 removes a anime")
    @Order(7)
    @SneakyThrows
    void delete_RemovesAnime_WhenSuccessful() {
        var anime = animesList.getFirst();
        var id = anime.getId();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(anime));

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /animes/99 throws BadRequestException when anime is not found")
    @Order(8)
    @SneakyThrows
    void delete_ThrowsBadRequestException_WhenAnimeIsNotFound() {
        var id = 99L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        var response = fileUtils.readResourceFiles("anime/delete-anime-by-id-400.json");


        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("PUT /animes updates a anime")
    @Order(9)
    @SneakyThrows
    void update_updatesAnime_WhenSuccessful() {
        var animeToUpdate = animesList.getFirst();
        var id = animeToUpdate.getId();
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(animeToUpdate));

        var request = fileUtils.readResourceFiles("anime/put-request-anime-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT /animes BadRequestException when anime is not found")
    @Order(10)
    @SneakyThrows
    void update_ThrowsBadRequestException_WhenAnimeIsNotFound() {
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        var request = fileUtils.readResourceFiles("anime/put-request-anime-404.json");
        var response = fileUtils.readResourceFiles("anime/put-anime-by-id-400.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postAnimesBadRequestSource")
    @DisplayName("POST /animes returns bad request when fields are invalid")
    @Order(11)
    @SneakyThrows
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> error) {
        var request = fileUtils.readResourceFiles("anime/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage())
                .contains(error);
    }

    private static Stream<Arguments> postAnimesBadRequestSource() {
        var allRequiredErrors = allRequiredErrors();

        return Stream.of(
                Arguments.of("post-request-anime-blank-field-400.json", allRequiredErrors),
                Arguments.of("post-request-anime-empty-field-400.json", allRequiredErrors),
                Arguments.of("post-request-anime-null-field-400.json", allRequiredErrors)
        );
    }


    private static List<String> allRequiredErrors() {
        var nameRequiredError = "The field 'name' is required";
        return new ArrayList<>(List.of(nameRequiredError));
    }

    @ParameterizedTest
    @MethodSource("putAnimesBadRequestSource")
    @DisplayName("PUT /animes returns bad request when fields are invalid")
    @Order(12)
    @SneakyThrows
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) {
        var request = fileUtils.readResourceFiles("anime/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage())
                .contains(errors);
    }

    private static Stream<Arguments> putAnimesBadRequestSource() {
        var allRequiredErrors = allRequiredErrors();
        allRequiredErrors.add("The field 'id' is required");

        return Stream.of(
                Arguments.of("put-request-anime-blank-field-400.json", allRequiredErrors),
                Arguments.of("put-request-anime-empty-field-400.json", allRequiredErrors),
                Arguments.of("put-request-anime-null-field-400.json", allRequiredErrors)
        );
    }


}