package io.github.danielcampossantos.repository;

import io.github.danielcampossantos.commons.AnimeUtils;
import io.github.danielcampossantos.domain.Anime;
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
class AnimeHardCodedRepositoryTest {
    @InjectMocks
    private AnimeHardCodedRepository repository;

    @Mock
    private AnimeData animeData;

    private List<Anime> animeList;

    @InjectMocks
    private AnimeUtils animeUtils;


    @BeforeEach
    void init() {
        animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("findAll returns list of animes")
    @Order(1)
    void findAll_ReturnsListOfAnimes_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animes = repository.findAll();

        Assertions.assertThat(animes)
                .isNotEmpty()
                .isNotNull()
                .hasSize(animeList.size());
    }

    @Test
    @DisplayName("findById returns anime by id")
    @Order(2)
    void findById_ReturnsAnimeById_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animeToBeFound = animeData.getAnimes().getFirst();

        var producer = repository.findById(animeToBeFound.getId());

        Assertions.assertThat(producer)
                .isNotEmpty()
                .isNotNull()
                .contains(animeToBeFound);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var producer = repository.findByName(null);

        Assertions.assertThat(producer)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findByName returns list with found object when name exists")
    @Order(4)
    void findByName_ReturnsFoundProducerInList_WhenNameIsFound() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var expectedAnime = animeData.getAnimes().getFirst();

        var animes = repository.findByName(expectedAnime.getName());

        Assertions.assertThat(animes).contains(expectedAnime);

    }


    @Test
    @DisplayName("save returns returns saved anime")
    @Order(5)
    void save_ReturnsSavedAnime_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animeToSave = animeUtils.newAnimeToSave();

        var savedAnime = repository.save(animeToSave);

        Assertions.assertThat(animeToSave).isNotNull().isEqualTo(savedAnime);


    }

    @Test
    @DisplayName("delete removes anime when successful")
    @Order(6)
    void delete_RemovesAnime_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animeToBeDeleted = animeData.getAnimes().getFirst();

        repository.delete(animeToBeDeleted);

        var animeOptional = repository.findById(animeToBeDeleted.getId());

        Assertions.assertThat(animeOptional).isNotNull().isNotPresent();


    }

    @Test
    @DisplayName("update updates anime")
    @Order(7)
    void update_UpdatesAnime_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animeToBeUpdated = animeData.getAnimes().getFirst();
        animeToBeUpdated.setName("NEW NAME");

        repository.update(animeToBeUpdated);

        Assertions.assertThat(animeData.getAnimes())
                .isNotNull()
                .isNotEmpty()
                .contains(animeToBeUpdated);

        var animeOptional = repository.findById(animeToBeUpdated.getId());

        Assertions.assertThat(animeOptional).isPresent();
        Assertions.assertThat(animeOptional.get().getName()).isEqualTo(animeToBeUpdated.getName());


    }
}