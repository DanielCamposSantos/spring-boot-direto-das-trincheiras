package io.github.danielcampossantos.controller;

import io.github.danielcampossantos.domain.Anime;
import io.github.danielcampossantos.mapper.AnimeMapper;
import io.github.danielcampossantos.requests.AnimePostRequest;
import io.github.danielcampossantos.requests.AnimePutRequest;
import io.github.danielcampossantos.response.AnimeGetResponse;
import io.github.danielcampossantos.response.AnimePostResponse;
import io.github.danielcampossantos.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("animes")
@Log4j2
@RequiredArgsConstructor

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

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable long id) {
        log.debug("Request to find anime by id '{}'", id);

        var anime = service.findByIdOrThrowBadRequestException(id);

        var animeGetResponse = mapper.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest) {
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
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest request) {
        log.debug("Request to update anime by id '{}'", request);

        var animeUpdated = mapper.toAnime(request);

        service.update(animeUpdated);

        return ResponseEntity.noContent().build();


    }

}
