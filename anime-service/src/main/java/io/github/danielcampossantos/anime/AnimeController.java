package io.github.danielcampossantos.anime;


import io.github.danielcampossantos.api.AnimeControllerApi;
import io.github.danielcampossantos.domain.Anime;
import io.github.danielcampossantos.dto.AnimeGetResponse;
import io.github.danielcampossantos.dto.AnimePostRequest;
import io.github.danielcampossantos.dto.AnimePostResponse;
import io.github.danielcampossantos.dto.AnimePutRequest;
import io.github.danielcampossantos.dto.PageAnimeGetResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("animes")
@Log4j2
@RequiredArgsConstructor

public class AnimeController implements AnimeControllerApi {

  private final AnimeMapper mapper;

  private final AnimeService service;


  @GetMapping
  public ResponseEntity<List<AnimeGetResponse>> findAllAnimes(@RequestParam(required = false) String name) {
    log.debug("Request to get list of animes by name '{}'", name);

    var animes = service.findAll(name);

    var animeGetResponses = mapper.toAnimeGetResponseList(animes);

    return ResponseEntity.ok(animeGetResponses);

  }

  @GetMapping("/paginated")
  public ResponseEntity<PageAnimeGetResponse> findAllAnimesPaginated(@ParameterObject Pageable pageable) {
    log.debug("Request to get list of animes paginated");

    var jpaAnimePageGetResponse = service.findAllPaginated(pageable);

    var pageAnimeGetResponse = mapper.toPageAnimeGetResponse(jpaAnimePageGetResponse);

    return ResponseEntity.ok(pageAnimeGetResponse);


  }

  @GetMapping("{id}")
  public ResponseEntity<AnimeGetResponse> findAnimeById(@PathVariable Long id) {
    log.debug("Request to find anime by id '{}'", id);

    var anime = service.findByIdOrThrowBadRequestException(id);

    var animeGetResponse = mapper.toAnimeGetResponse(anime);

    return ResponseEntity.ok(animeGetResponse);
  }

  @PostMapping
  public ResponseEntity<AnimePostResponse> saveAnime(@RequestBody @Valid AnimePostRequest animePostRequest) {
    log.debug("Request to create anime '{}'", animePostRequest);
    var anime = mapper.toAnime(animePostRequest);

    Anime savedAnime = service.save(anime);

    var response = mapper.toAnimePostResponse(savedAnime);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteAnime(@PathVariable Long id) {
    log.debug("Request to delete anime by id '{}'", id);

    service.delete(id);

    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> updateAnime(@RequestBody @Valid AnimePutRequest request) {
    log.debug("Request to update anime by id '{}'", request);

    var animeUpdated = mapper.toAnime(request);

    service.update(animeUpdated);

    return ResponseEntity.noContent().build();


  }

}
