package io.github.danielcampossantos.anime;

import io.github.danielcampossantos.domain.Anime;
import io.github.danielcampossantos.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository repository;

    public List<Anime> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByNameIgnoreCase(name);
    }

    public Page<Anime> findAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Anime findByIdOrThrowBadRequestException(Long id) {
        return repository.findById(id).orElseThrow(this::throwBadRequestException);
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

    private BadRequestException throwBadRequestException() {
        return new BadRequestException("Anime not found");
    }
}
