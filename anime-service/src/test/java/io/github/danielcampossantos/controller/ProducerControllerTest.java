package io.github.danielcampossantos.controller;

import io.github.danielcampossantos.domain.Producer;
import io.github.danielcampossantos.mapper.ProducerMapperImpl;
import io.github.danielcampossantos.repository.ProducerData;
import io.github.danielcampossantos.repository.ProducerHardCodedRepository;
import io.github.danielcampossantos.service.ProducerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = ProducerController.class)
@Import({ProducerMapperImpl.class, ProducerService.class, ProducerHardCodedRepository.class, ProducerData.class})
class ProducerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProducerData producerData;

    @MockitoSpyBean
    private ProducerHardCodedRepository repository;

    @Autowired
    private ResourceLoader resourceLoader;


    private List<Producer> producerList;

    @BeforeEach
    void init() {
        var dateTime = "2026-02-27T15:25:51.7839578";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, formatter);


        var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build();
        var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(localDateTime).build();
        var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(localDateTime).build();
        producerList = new ArrayList<>(List.of(ufotable, witStudio, studioGhibli));
    }


    @Test
    @DisplayName("GET /producers returns list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsListWithProducers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var response = getResource("producer/get-producer-null-name-200.json");


        mockMvc.perform(MockMvcRequestBuilders.get("/producers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET /producers&name=Ufotable returns found anime in list when name exists")
    @Order(2)
    void findAll_ReturnsFoundAnimeInList_WhenNameExists() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var response = getResource("producer/get-producer-ufotable-name-200.json");
        var name = "Ufotable";

        mockMvc.perform(MockMvcRequestBuilders.get("/producers").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET /producers?name=x returns empty list of animes when argument is not found")
    @Order(3)
    void findAll_ReturnsEmptyListOfAnimes_WhenArgumentIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var response = getResource("producer/get-producer-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get("/producers").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }


    @Test
    @DisplayName("GET producers/1 returns producer by id")
    @Order(4)
    void findById_ReturnsProducerById_WhenSuccssesful() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var response = getResource("producer/get-producer-by-id-200.json");
        var id = 1L;


        mockMvc.perform(MockMvcRequestBuilders.get("/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }


    @Test
    @DisplayName("GET producers/99 throws ResponseStatusException 400 when producer is not found")
    @Order(4)
    void findById_ThrowsBadRequestException_WhenProducerIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get("/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));

    }


    @Test
    @DisplayName("POST /producers creates a producer")
    @Order(5)
    void save_CreatesProducer_WhenSuccessful() throws Exception {
        var request = getResource("producer/post-request-producer-200.json");
        var response = getResource("producer/post-response-producer-201.json");
        var producerToSave = Producer.builder()
                .name("MAPPA")
                .id(99L)
                .createdAt(LocalDateTime.now())
                .build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/producers")
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
    void update_updatesProducer_WhenSuccessful() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var request = getResource("producer/put-request-producer-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/producers")
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

        var request = getResource("producer/put-request-producer-404.json");
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/producers")
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

        mockMvc.perform(MockMvcRequestBuilders.delete("/producers/{id}", id))
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

        mockMvc.perform(MockMvcRequestBuilders.delete("/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));

    }


    private String getResource(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }

}