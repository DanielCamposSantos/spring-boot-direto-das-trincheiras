package io.github.danielcampossantos.controller;

import io.github.danielcampossantos.commons.FileUtils;
import io.github.danielcampossantos.commons.ProducerUtils;
import io.github.danielcampossantos.domain.Producer;
import io.github.danielcampossantos.repository.ProducerData;
import io.github.danielcampossantos.repository.ProducerHardCodedRepository;
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
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = ProducerController.class)
@ComponentScan(basePackages = {"io.github.danielcampossantos"})
class ProducerControllerTest {
    private static final String URL = "/producers";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProducerData producerData;

    @MockitoSpyBean
    private ProducerHardCodedRepository repository;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private ProducerUtils producerUtils;


    private List<Producer> producerList;

    @BeforeEach
    void init() {
        producerList = producerUtils.newProducerList();
    }


    @Test
    @DisplayName("GET /producers returns list with all producers when argument is null")
    @Order(1)

    @SneakyThrows
    void findAll_ReturnsListWithProducers_WhenArgumentIsNull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var response = fileUtils.readResourceFiles("producer/get-producer-null-name-200.json");


        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET /producers&name=Ufotable returns found anime in list when name exists")
    @Order(2)

    @SneakyThrows
    void findAll_ReturnsFoundAnimeInList_WhenNameExists() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var response = fileUtils.readResourceFiles("producer/get-producer-ufotable-name-200.json");
        var name = "Ufotable";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET /producers?name=x returns empty list of animes when argument is not found")
    @Order(3)

    @SneakyThrows
    void findAll_ReturnsEmptyListOfAnimes_WhenArgumentIsNotFound() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var response = fileUtils.readResourceFiles("producer/get-producer-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }


    @Test
    @DisplayName("GET producers/1 returns producer by id")
    @Order(4)

    @SneakyThrows
    void findById_ReturnsProducerById_WhenSuccssesful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var response = fileUtils.readResourceFiles("producer/get-producer-by-id-200.json");
        var id = 1L;


        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }


    @Test
    @DisplayName("GET producers/99 throws ResponseStatusException 400 when producer is not found")
    @Order(4)

    @SneakyThrows
    void findById_ThrowsBadRequestException_WhenProducerIsNotFound() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));

    }


    @Test
    @DisplayName("POST /producers creates a producer")
    @Order(5)
    @SneakyThrows
    void save_CreatesProducer_WhenSuccessful() {
        var request = fileUtils.readResourceFiles("producer/post-request-producer-200.json");
        var response = fileUtils.readResourceFiles("producer/post-response-producer-201.json");

        var producerToSave = producerUtils.newProducerToSave();
        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("PUT /producers updates a producer")
    @Order(7)

    @SneakyThrows
    void update_updatesProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var request = fileUtils.readResourceFiles("producer/put-request-producer-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT /producers ResponseStatusException when producer is not found")
    @Order(7)
    @SneakyThrows
    void update_ThrowsResponseStatusException_WhenProducerIsNotFound() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var request = fileUtils.readResourceFiles("producer/put-request-producer-404.json");
        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));
    }

    @Test
    @DisplayName("DELETE /producers/1 removes a producer")
    @Order(9)
    @SneakyThrows
    void delete_RemovesProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @DisplayName("DELETE /producers/99 throws ResponseStatusException when producer is not found")
    @Order(10)
    @SneakyThrows
    void delete_ThrowsResponseStatusException_WhenProducerIsNotFound() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));

    }

    @ParameterizedTest
    @MethodSource("postBadRequestource")
    @DisplayName("POST /producers returns bad request when fields are invalid")
    @Order(11)
    @SneakyThrows
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) {
        var request = fileUtils.readResourceFiles("producer/%s".formatted(fileName));

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
                .contains(errors);
    }

    private static Stream<Arguments> postBadRequestource() {
        var allRequiredErrors = allRequiredErrors();

        return Stream.of(
                Arguments.of("post-request-producer-blank-field-400.json", allRequiredErrors),
                Arguments.of("post-request-producer-empty-field-400.json", allRequiredErrors),
                Arguments.of("post-request-producer-null-field-400.json", allRequiredErrors)
        );
    }

    private static List<String> allRequiredErrors() {
        var nameRequiredError = "The field 'name' is required";
        return new ArrayList<>(List.of(nameRequiredError));
    }


    @ParameterizedTest
    @MethodSource("putBadRequestSource")
    @DisplayName("PUT /producers ResponseStatusException when producer is not found")
    @Order(7)
    @SneakyThrows
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var request = fileUtils.readResourceFiles("producer/%s".formatted(fileName));
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

    private static Stream<Arguments> putBadRequestSource() {
        var allRequiredErrors = allRequiredErrors();
        allRequiredErrors.add("The field 'id' is required");

        return Stream.of(
                Arguments.of("put-request-producer-blank-field-400.json", allRequiredErrors),
                Arguments.of("put-request-producer-empty-field-400.json", allRequiredErrors),
                Arguments.of("put-request-producer-null-field-400.json", allRequiredErrors)
        );
    }


}