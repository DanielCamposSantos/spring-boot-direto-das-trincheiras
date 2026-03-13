package io.github.danielcampossantos.anime.service;

import io.github.danielcampossantos.anime.repository.AnimeRepository;
import io.github.danielcampossantos.domain.Anime;
import io.github.danielcampossantos.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository repository;

    public List<Anime> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByNameIgnoreCase(name);
    }

    public Anime findByIdOrThrowBadRequestException(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not found"));
    }

    public Anime save(Anime anime) {
        return repository.save(anime);
    }

    public void delete(long id) {
        var animeToDelete = findByIdOrThrowBadRequestException(id);
        repository.delete(animeToDelete);
    }

    public void update(Anime animeToBeUpdated) {
        assertAnimeExists(animeToBeUpdated.getId());

        repository.save(animeToBeUpdated);
    }

    public void assertAnimeExists(long id) {
        findByIdOrThrowBadRequestException(id);
    }

}
