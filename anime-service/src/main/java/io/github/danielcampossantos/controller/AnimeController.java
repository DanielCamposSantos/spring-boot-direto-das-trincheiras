package io.github.danielcampossantos.controller;

import io.github.danielcampossantos.domain.Anime;
import io.github.danielcampossantos.mapper.AnimeMapper;
import io.github.danielcampossantos.requests.AnimePostRequest;
import io.github.danielcampossantos.response.AnimeGetResponse;
import io.github.danielcampossantos.response.AnimePostResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("animes")
@Log4j2
public class AnimeController {

    private static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> listAll(@RequestParam(required = false) String name) {
        log.debug("Request to get list of animes by name '{}'", name);
        var animes = Anime.getAnimes();
        var animeGetResponseList = MAPPER.toAnimeGetResponseList(animes);
        if (name == null) return ResponseEntity.ok(animeGetResponseList);
        var animeList = animeGetResponseList.stream()
                .filter(a -> a.name().equalsIgnoreCase(name))
                .toList();

        return ResponseEntity.ok(animeList);

    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> getById(@PathVariable long id) {
        log.debug("Request to find anime by id '{}'", id);

        var animeGetResponse = Anime.getAnimes().stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst()
                .map(MAPPER::toAnimeGetResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found"));

        return ResponseEntity.ok(animeGetResponse);

    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest) {
        log.debug("Request to create anime '{}'", animePostRequest);
        var response = MAPPER.toAnimePostResponse(animePostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        log.debug("Deleting anime by id '{}'", id);

        var animeToBeDeleted = Anime.getAnimes().stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found"));

        Anime.getAnimes().remove(animeToBeDeleted);

        return ResponseEntity.noContent().build();
    }


}
