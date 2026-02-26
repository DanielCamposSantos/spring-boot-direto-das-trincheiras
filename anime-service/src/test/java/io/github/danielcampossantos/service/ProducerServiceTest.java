package io.github.danielcampossantos.service;

import io.github.danielcampossantos.domain.Producer;
import io.github.danielcampossantos.repository.ProducerHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerServiceTest {
    @InjectMocks
    private ProducerService service;

    @Mock
    private ProducerHardCodedRepository repository;


    private List<Producer> producerList;

    @BeforeEach
    void init() {
        var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
        var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(LocalDateTime.now()).build();
        var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build();
        producerList = new ArrayList<>(List.of(ufotable, witStudio, studioGhibli));
    }

    @Test
    @DisplayName("findAll returns list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsListWithProducers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(producerList);

        var expectedProducers = repository.findAll();

        var producers = service.findAll(null);

        Assertions.assertThat(expectedProducers)
                .isNotNull()
                .hasSameElementsAs(producers);
    }

    @Test
    @DisplayName("findAll returns found producer in list when name exists")
    @Order(2)
    void findAll_ReturnsFoundProducerInList_WhenNameExists() {
        var producer = producerList.getFirst();
        var expectedProducersFound = Collections.singletonList(producer);
        BDDMockito.when(repository.findByName(producer.getName())).thenReturn(expectedProducersFound);

        var producers = service.findAll(producer.getName());

        Assertions.assertThat(producers).containsAll(expectedProducersFound);


    }

    @Test
    @DisplayName("findAll returns empty list of producers when argument is not found")
    @Order(3)
    void findAll_ReturnsEmptyListOfProducers_WhenArgumentIsNotFound() {
        var name = "not-found";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

        var producers = service.findAll(name);

        Assertions.assertThat(producers)
                .isNotNull()
                .isEmpty();


    }

    @Test
    @DisplayName("findById returns producer by id")
    @Order(4)
    void findById_ReturnsProducerById_WhenSuccssesful() {
        var expectedProducer = producerList.getFirst();

        BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.of(expectedProducer));

        var producerOptional = repository.findById(expectedProducer.getId());

        var producer = service.findByIdOrThrowBadRequest(expectedProducer.getId());

        Assertions.assertThat(producerOptional)
                .isPresent()
                .isNotNull()
                .hasValue(producer);
    }


    @Test
    @DisplayName("findById throws ResponseStatusException when producer is not found")
    @Order(4)
    void findById_ThrowsBadRequestException_WhenProducerIsNotFound() {
        var expectedProducer = Producer.builder().id(99L).build();

        BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.empty());

        var producerOptional = repository.findById(expectedProducer.getId());

        Assertions.assertThat(producerOptional)
                .isNotPresent()
                .isNotNull();

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowBadRequest(expectedProducer.getId()))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

    }


    @Test
    @DisplayName("save creates a producer")
    @Order(5)
    void save_CreatesProducer_WhenSuccessful() {
        var producerToSave = Producer.builder()
                .name("NEW PRODUCER")
                .id(99L)
                .createdAt(LocalDateTime.now())
                .build();

        BDDMockito.when(repository.save(producerToSave)).thenReturn(producerToSave);

        var savedProducer = service.save(producerToSave);

        Assertions.assertThat(producerToSave).isEqualTo(savedProducer);
    }


    @Test
    @DisplayName("delete removes a producer")
    @Order(6)
    void delete_RemovesProducer_WhenSuccessful() {
        var producerToDelete = producerList.getLast();

        BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.of(producerToDelete));
        BDDMockito.doNothing().when(repository).delete(producerToDelete);

        Assertions.assertThatNoException().isThrownBy(()-> service.delete(producerToDelete.getId()));

    }

    @Test
    @DisplayName("delete throws ResponseStatusException when producer is not found")
    @Order(6)
    void delete_ThrowsResponseStatusException_WhenProducerIsNotFound() {
        var producerToDelete = producerList.getLast();
        BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(()-> service.delete(producerToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

    }


    @Test
    @DisplayName("update updates a producer")
    @Order(7)
    void update_updatesProducer_WhenSuccessful() {
        var producerToBeUpdated = producerList.getLast();
        producerToBeUpdated.setName("UPDATED PRODUCER NAME");

        BDDMockito.when(repository.findById(producerToBeUpdated.getId())).thenReturn(Optional.of(producerToBeUpdated));
        BDDMockito.doNothing().when(repository).update(producerToBeUpdated);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(producerToBeUpdated));

    }

    @Test
    @DisplayName("update ResponseStatusException when producer is not found")
    @Order(7)
    void update_ThrowsResponseStatusException_WhenProducerIsNotFound() {
        var producerToBeUpdated = producerList.getLast();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(()-> service.update(producerToBeUpdated))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }



}