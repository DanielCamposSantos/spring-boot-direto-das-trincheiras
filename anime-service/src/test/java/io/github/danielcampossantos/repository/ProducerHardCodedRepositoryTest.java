package io.github.danielcampossantos.repository;

import io.github.danielcampossantos.commons.ProducerUtils;
import io.github.danielcampossantos.domain.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerHardCodedRepositoryTest {

    @InjectMocks
    private ProducerHardCodedRepository repository;

    @Mock
    private ProducerData producerData;

    private List<Producer> producerList;

    @InjectMocks
    private ProducerUtils producerUtils;

    @BeforeEach
    void init() {
        producerList = producerUtils.newProducerList();
    }

    @Test
    @DisplayName("findAll returns list with producers")
    @Order(1)
    void findAll_ReturnsListWithProducers_WhenSuccssesful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producers = repository.findAll();

        Assertions.assertThat(producers)
                .isNotEmpty()
                .isNotNull()
                .containsAnyElementsOf(producerList);
    }

    @Test
    @DisplayName("findById returns producer by id")
    @Order(2)
    void findById_ReturnsProducerById_WhenSuccssesful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var expectedProducer = producerData.getProducers().getFirst();

        var producers = repository.findById(expectedProducer.getId());

        Assertions.assertThat(producers)
                .isNotEmpty()
                .isNotNull()
                .contains(expectedProducer);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producer = repository.findByName(null);

        Assertions.assertThat(producer)
                .isNotNull()
                .isEmpty();
    }


    @Test
    @DisplayName("findByName returns found producer in list when name exists")
    @Order(4)
    void findByName_ReturnsFoundProducerInList_WhenNameExists() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var expectedProducer = producerData.getProducers().getFirst();

        var producer = repository.findByName(expectedProducer.getName());

        Assertions.assertThat(producer)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(expectedProducer);
    }

    @Test
    @DisplayName("save creates a producer")
    @Order(5)
    void save_CreatesProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerToSave = producerUtils.newProducerToSave();

        var producer = repository.save(producerToSave);

        Assertions.assertThat(producer).hasNoNullFieldsOrProperties().isEqualTo(producerToSave);

        var producerOptional = repository.findById(producer.getId());

        Assertions.assertThat(producerOptional).isPresent().contains(producerToSave);
    }

    @Test
    @DisplayName("delete removes a producer")
    @Order(6)
    void delete_RemovesProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerToBeRemoved = producerData.getProducers().getLast();

        repository.delete(producerToBeRemoved);

        var producers = repository.findAll();

        Assertions.assertThat(producers).isNotEmpty().doesNotContain(producerToBeRemoved);

    }

    @Test
    @DisplayName("update updates a producer")
    @Order(7)
    void update_updatesProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerToBeUpdated = producerData.getProducers().getLast();
        producerToBeUpdated.setName("UPDATED PRODUCER NAME");

        repository.update(producerToBeUpdated);

        Assertions.assertThat(this.producerList).isNotEmpty().contains(producerToBeUpdated);

        var producerOptional = repository.findById(producerToBeUpdated.getId());

        Assertions.assertThat(producerOptional).isPresent();
        Assertions.assertThat(producerOptional.get().getName()).isEqualTo(producerToBeUpdated.getName());

    }

}