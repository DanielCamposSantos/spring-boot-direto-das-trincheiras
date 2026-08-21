package io.github.danielcampossantos.producer;


import io.github.danielcampossantos.api.ProducerControllerApi;
import io.github.danielcampossantos.dto.ProducerGetResponse;
import io.github.danielcampossantos.dto.ProducerPostRequest;
import io.github.danielcampossantos.dto.ProducerPostResponse;
import io.github.danielcampossantos.dto.ProducerPutRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@RestController
@RequestMapping("producers")
@RequiredArgsConstructor
public class ProducerController implements ProducerControllerApi {

  private final ProducerMapper mapper;
  private final ProducerService service;


  @GetMapping
  public ResponseEntity<List<ProducerGetResponse>> findAllProducers(@RequestParam(required = false) String name) {
    log.debug("Request to get list of producers by name '{}'", name);

    var producers = service.findAll(name);
    var producerGetResponses = mapper.toProducerGetResponseList(producers);

    return ResponseEntity.ok(producerGetResponses);
  }

  @GetMapping(path = "{id}")
  public ResponseEntity<ProducerGetResponse> findProducerById(@PathVariable Long id) {
    log.debug("Request to get producer by id '{}'", id);
    var producerGetResponse = mapper.toProducerGetResponse(service.findByIdOrThrowBadRequest(id));
    return ResponseEntity.ok(producerGetResponse);
  }


  @PostMapping
  public ResponseEntity<ProducerPostResponse> saveProducer(@RequestBody @Valid ProducerPostRequest producerPostRequest) {
    log.debug("Saving producer '{}'", producerPostRequest);

    var producerToSave = mapper.toProducer(producerPostRequest);

    var producer = service.save(producerToSave);

    var response = mapper.toProducerPostResponse(producer);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteProducer(@PathVariable Long id) {
    log.debug("Request to delete producer by id '{}'", id);

    service.delete(id);

    return ResponseEntity.noContent().build();
  }


  @PutMapping
  public ResponseEntity<Void> updateProducer(@RequestBody @Valid ProducerPutRequest request) {
    log.debug("Request to update producer '{}'", request);

    var producerToUpdate = mapper.toProducer(request);

    service.update(producerToUpdate);

    return ResponseEntity.noContent().build();
  }


}
