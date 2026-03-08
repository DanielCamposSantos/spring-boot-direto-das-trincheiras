package io.github.danielcampossantos.controller;


import io.github.danielcampossantos.mapper.ProducerMapper;
import io.github.danielcampossantos.requests.ProducerPostRequest;
import io.github.danielcampossantos.requests.ProducerPutRequest;
import io.github.danielcampossantos.response.ProducerGetResponse;
import io.github.danielcampossantos.response.ProducerPostResponse;
import io.github.danielcampossantos.service.ProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("producers")
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerMapper mapper;
    private final ProducerService service;


    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request to get list of producers by name '{}'", name);

        var producers = service.findAll(name);
        var producerGetResponses = mapper.toProducerGetResponseList(producers);

        return ResponseEntity.ok(producerGetResponses);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable long id) {
        log.debug("Request to get producer by id '{}'", id);
        var producerGetResponse = mapper.toProducerGetResponse(service.findByIdOrThrowBadRequest(id));
        return ResponseEntity.ok(producerGetResponse);
    }


    @PostMapping
    public ResponseEntity<ProducerPostResponse> save(@RequestBody @Valid ProducerPostRequest producerPostRequest) {
        log.debug("Saving producer '{}'", producerPostRequest);

        var producerToSave = mapper.toProducer(producerPostRequest);

        var producer = service.save(producerToSave);

        var response = mapper.toProducerPostResponse(producer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        log.debug("Request to delete producer by id '{}'", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }


    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid ProducerPutRequest request) {
        log.debug("Request to update producer '{}'", request);

        var producerToUpdate = mapper.toProducer(request);

        service.update(producerToUpdate);

        return ResponseEntity.noContent().build();
    }


}
