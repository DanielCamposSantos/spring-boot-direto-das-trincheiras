package io.github.danielcampossantos.producer;

import io.github.danielcampossantos.commons.FileUtils;
import io.github.danielcampossantos.commons.ProducerUtils;
import io.github.danielcampossantos.domain.Producer;
import io.github.danielcampossantos.producer.controller.ProducerController;
import io.github.danielcampossantos.producer.repository.ProducerRepository;
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
@WebMvcTest(controllers = ProducerController.class)
@ComponentScan(basePackages = {"io.github.danielcampossantos.producer", "io.github.danielcampossantos.commons"})
class ProducerControllerTest {
    private static final String URL = "/producers";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProducerRepository repository;

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
        BDDMockito.when(repository.findAll()).thenReturn(producerList);

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
        var producer = producerList.getFirst();
        var name = producer.getName();
        BDDMockito.when(repository.findByNameIgnoreCase(name)).thenReturn(Collections.singletonList(producer));

        var response = fileUtils.readResourceFiles("producer/get-producer-ufotable-name-200.json");

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
        BDDMockito.when(repository.findByNameIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

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
        var producer = producerList.getFirst();
        var id = producer.getId();
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(producer));

        var response = fileUtils.readResourceFiles("producer/get-producer-by-id-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }


    @Test
    @DisplayName("GET producers/99 throws BadRequestException 400 when producer is not found")
    @Order(4)

    @SneakyThrows
    void findById_ThrowsBadRequestException_WhenProducerIsNotFound() {
        var id = 99L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        var response = fileUtils.readResourceFiles("producer/get-producer-by-id-400.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(response));

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
        var producerToUpdate = producerList.getFirst();
        var id = producerToUpdate.getId();
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(producerToUpdate));

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
    @DisplayName("PUT /producers BadRequestException when producer is not found")
    @Order(7)
    @SneakyThrows
    void update_ThrowsBadRequestException_WhenProducerIsNotFound() {
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        var request = fileUtils.readResourceFiles("producer/put-request-producer-404.json");
        var response = fileUtils.readResourceFiles("producer/put-producer-by-id-400.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE /producers/1 removes a producer")
    @Order(9)
    @SneakyThrows
    void delete_RemovesProducer_WhenSuccessful() {
        var producer = producerList.getFirst();
        var id = producer.getId();
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(producer));


        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @DisplayName("DELETE /producers/99 throws BadRequestException when producer is not found")
    @Order(10)
    @SneakyThrows
    void delete_ThrowsBadRequestException_WhenProducerIsNotFound() {
        var id = 99L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());
        var response = fileUtils.readResourceFiles("producer/delete-producer-by-id-400.json");


        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }


    @ParameterizedTest
    @MethodSource("postProducersBadRequestSources")
    @DisplayName("POST /producers returns bad request when fields are invalid")
    @Order(5)
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

    private static Stream<Arguments> postProducersBadRequestSources() {
        var allRequiredErrors = allRequiredErrors();
        return Stream.of(
                Arguments.of("post-request-producer-empty-field-400.json", allRequiredErrors),
                Arguments.of("post-request-producer-blank-field-400.json", allRequiredErrors),
                Arguments.of("post-request-producer-null-field-400.json", allRequiredErrors)
        );
    }

    private static List<String> allRequiredErrors() {
        var nameRequiredError = "The field 'name' is required";
        return new ArrayList<>(List.of(nameRequiredError));
    }

    @ParameterizedTest
    @MethodSource("putProducersBadRequestSources")
    @DisplayName("PUT /producers returns bad request when fields are invalid")
    @Order(5)
    @SneakyThrows
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) {
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

    private static Stream<Arguments> putProducersBadRequestSources() {
        var allRequiredErrors = allRequiredErrors();
        allRequiredErrors.add("The field 'id' is required");
        return Stream.of(
                Arguments.of("put-request-producer-empty-field-400.json", allRequiredErrors),
                Arguments.of("put-request-producer-blank-field-400.json", allRequiredErrors),
                Arguments.of("put-request-producer-null-field-400.json", allRequiredErrors)
        );
    }

}