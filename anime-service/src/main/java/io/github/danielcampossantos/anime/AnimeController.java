package io.github.danielcampossantos.anime;

import io.github.danielcampossantos.domain.Anime;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("animes")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")

public class AnimeController {
    private final AnimeMapper mapper;

    private final AnimeService service;

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request to get list of animes by name '{}'", name);

        var animes = service.findAll(name);

        var animeGetResponses = mapper.toAnimeGetResponseList(animes);

        return ResponseEntity.ok(animeGetResponses);

    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<AnimeGetResponse>> findAllPaginated(Pageable pageable) {
        log.debug("Request to get list of animes paginated");

        var pageAnime = service.findAllPaginated(pageable).map(mapper::toAnimeGetResponse);

        return ResponseEntity.ok(pageAnime);

    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable long id) {
        log.debug("Request to find anime by id '{}'", id);

        var anime = service.findByIdOrThrowBadRequestException(id);

        var animeGetResponse = mapper.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody @Valid AnimePostRequest animePostRequest) {
        log.debug("Request to create anime '{}'", animePostRequest);
        var anime = mapper.toAnime(animePostRequest);

        Anime savedAnime = service.save(anime);

        var response = mapper.toAnimePostResponse(savedAnime);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        log.debug("Request to delete anime by id '{}'", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid AnimePutRequest request) {
        log.debug("Request to update anime by id '{}'", request);

        var animeUpdated = mapper.toAnime(request);

        service.update(animeUpdated);

        return ResponseEntity.noContent().build();


    }

}
