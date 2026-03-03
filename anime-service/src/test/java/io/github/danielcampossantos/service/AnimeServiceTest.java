package io.github.danielcampossantos.service;

import io.github.danielcampossantos.commons.AnimeUtils;
import io.github.danielcampossantos.domain.Anime;
import io.github.danielcampossantos.repository.AnimeHardCodedRepository;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService service;

    @Mock
    private AnimeHardCodedRepository repository;

    @InjectMocks
    private AnimeUtils animeUtils;


    private List<Anime> animesList;

    @BeforeEach
    void init() {
       animesList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("findAll returns list with all animes when argument is null")
    @Order(1)
    void findAll_ReturnsListWithAnimes_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(animesList);

        var expectedAnimes = repository.findAll();

        var animes = service.findAll(null);

        Assertions.assertThat(expectedAnimes)
                .isNotNull()
                .hasSameElementsAs(animes);
    }

    @Test
    @DisplayName("findAll returns found anime in list when name exists")
    @Order(2)
    void findAll_ReturnsFoundAnimeInList_WhenNameExists() {
        var anime = animesList.getFirst();
        var expectedAnimesFound = Collections.singletonList(anime);
        BDDMockito.when(repository.findByName(anime.getName())).thenReturn(expectedAnimesFound);

        var animes = service.findAll(anime.getName());

        Assertions.assertThat(animes).containsAll(expectedAnimesFound);


    }

    @Test
    @DisplayName("findAll returns empty list of animes when argument is not found")
    @Order(3)
    void findAll_ReturnsEmptyListOfAnimes_WhenArgumentIsNotFound() {
        var name = "not-found";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

        var animes = service.findAll(name);

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();


    }

    @Test
    @DisplayName("findById returns anime by id")
    @Order(4)
    void findById_ReturnsAnimeById_WhenSuccssesful() {
        var expectedAnime = animesList.getFirst();

        BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.of(expectedAnime));

        var animeOptional = repository.findById(expectedAnime.getId());

        var anime = service.findByIdOrThrowBadRequestException(expectedAnime.getId());

        Assertions.assertThat(animeOptional)
                .isPresent()
                .isNotNull()
                .hasValue(anime);
    }


    @Test
    @DisplayName("findById throws ResponseStatusException when anime is not found")
    @Order(5)
    void findById_ThrowsBadRequestException_WhenAnimeIsNotFound() {
        var expectedAnime = Anime.builder().id(99L).build();

        BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.empty());

        var animeOptional = repository.findById(expectedAnime.getId());

        Assertions.assertThat(animeOptional)
                .isNotPresent()
                .isNotNull();

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowBadRequestException(expectedAnime.getId()))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

    }


    @Test
    @DisplayName("save creates a anime")
    @Order(6)
    void save_CreatesAnime_WhenSuccessful() {
        var animeToSave = animeUtils.newAnimeToSave();

        BDDMockito.when(repository.save(animeToSave)).thenReturn(animeToSave);

        var savedAnime = service.save(animeToSave);

        Assertions.assertThat(animeToSave).isEqualTo(savedAnime);
    }


    @Test
    @DisplayName("delete removes a anime")
    @Order(7)
    void delete_RemovesAnime_WhenSuccessful() {
        var animeToDelete = animesList.getLast();

        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));
        BDDMockito.doNothing().when(repository).delete(animeToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToDelete.getId()));

    }

    @Test
    @DisplayName("delete throws ResponseStatusException when anime is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var animeToDelete = animesList.getLast();
        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.delete(animeToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

    }


    @Test
    @DisplayName("update updates a anime")
    @Order(9)
    void update_updatesAnime_WhenSuccessful() {
        var animeToBeUpdated = animesList.getLast();
        animeToBeUpdated.setName("UPDATED PRODUCER NAME");

        BDDMockito.when(repository.findById(animeToBeUpdated.getId())).thenReturn(Optional.of(animeToBeUpdated));
        BDDMockito.doNothing().when(repository).update(animeToBeUpdated);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(animeToBeUpdated));

    }

    @Test
    @DisplayName("update ResponseStatusException when anime is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var animeToBeUpdated = animesList.getLast();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.update(animeToBeUpdated))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }


}