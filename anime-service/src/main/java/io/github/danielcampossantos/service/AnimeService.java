package io.github.danielcampossantos.service;

import io.github.danielcampossantos.domain.Anime;
import io.github.danielcampossantos.repository.AnimeHardCodedRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class AnimeService {
    private final AnimeHardCodedRepository repository;

    public AnimeService() {
        this.repository = new AnimeHardCodedRepository();
    }

    public List<Anime> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Anime findByIdOrThrowBadRequestException(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producer not found"));
    }

    public Anime save(Anime anime) {
        return repository.save(anime);
    }

    public void delete(long id) {
        var animeToDelete = findByIdOrThrowBadRequestException(id);
        repository.delete(animeToDelete);
    }

    public void update(Anime animeToBeUpdated) {
        var anime = findByIdOrThrowBadRequestException(animeToBeUpdated.getId());
        repository.delete(anime);
    }


}
