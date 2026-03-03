package io.github.danielcampossantos.controller;

import io.github.danielcampossantos.commons.AnimeUtils;
import io.github.danielcampossantos.commons.FileUtils;
import io.github.danielcampossantos.domain.Anime;
import io.github.danielcampossantos.repository.AnimeData;
import io.github.danielcampossantos.repository.AnimeHardCodedRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = AnimeController.class)
@ComponentScan(basePackages = {"io.github.danielcampossantos", "external.dependency"})
class AnimeControllerTest {
    private static final String URL = "/animes";

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private AnimeHardCodedRepository repository;

    @MockitoBean
    private AnimeData animeData;

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
    void findAll_ReturnsListWithAnimes_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var response = fileUtils.readResourceFiles("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET /animes?name=Mashle returns found anime in list when name exists")
    @Order(2)
    void findAll_ReturnsFoundAnimeInList_WhenNameExists() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var response = fileUtils.readResourceFiles("anime/get-anime-mashle-name-200.json");
        var name = "Mashle";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET /animes?name=x returns empty list of animes when argument is not found")
    @Order(3)
    void findAll_ReturnsEmptyListOfAnimes_WhenArgumentIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var response = fileUtils.readResourceFiles("anime/get-anime-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET /animes/3 returns anime by id")
    @Order(4)
    void findById_ReturnsAnimeById_WhenSuccssesful() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var response = fileUtils.readResourceFiles("anime/get-anime-by-id-200.json");
        var id = 3L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET /animes/99 throws ResponseStatusException 400 when anime is not found")
    @Order(5)
    void findById_ThrowsBadRequestException_WhenAnimeIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));

    }


    @Test
    @DisplayName("PUT /animes creates a anime")
    @Order(6)
    void save_CreatesAnime_WhenSuccessful() throws Exception {
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
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /animes/1 throws ResponseStatusException when anime is not found")
    @Order(8)
    @SneakyThrows
    void delete_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));

    }

    @Test
    @DisplayName("PUT /animes updates a anime")
    @Order(9)
    @SneakyThrows
    void update_updatesAnime_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var request = fileUtils.readResourceFiles("anime/put-request-anime-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT /animes ResponseStatusException when anime is not found")
    @Order(10)
    @SneakyThrows
    void update_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var request = fileUtils.readResourceFiles("anime/put-request-anime-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));
    }


}