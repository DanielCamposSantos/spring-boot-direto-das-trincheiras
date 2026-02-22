package io.github.danielcampossantos.controller;


import io.github.danielcampossantos.mapper.ProducerMapper;
import io.github.danielcampossantos.requests.ProducerPostRequest;
import io.github.danielcampossantos.requests.ProducerPutRequest;
import io.github.danielcampossantos.response.ProducerGetResponse;
import io.github.danielcampossantos.service.ProducerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("producers")
public class ProducerController {

    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    private ProducerService service;

    public ProducerController() {
        this.service = new ProducerService();
    }


    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request to get list of producers by name '{}'", name);

        var producers = service.findAll(name);
        var producerGetResponses = MAPPER.toProducerGetResponseList(producers);

        return ResponseEntity.ok(producerGetResponses);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable long id) {
        log.debug("Request to get producer by id '{}'", id);
        var producerGetResponse = MAPPER.toProducerGetResponse(service.findByIdOrThrowBadRequest(id));
        return ResponseEntity.ok(producerGetResponse);

    }


    @PostMapping
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest) {
        log.debug("Saving producer '{}'", producerPostRequest);

        var producer = MAPPER.toProducer(producerPostRequest);

        service.save(producer);

        var response = MAPPER.toProducerGetResponse(producer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        log.debug("Request to delete producer by id '{}'", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }


    @PutMapping
    public ResponseEntity<Void> update(@RequestBody ProducerPutRequest request) {
        log.debug("Request to update producer '{}'", request);


        var producerToUpdate = MAPPER.toProducer(request);

        service.update(producerToUpdate);

        return ResponseEntity.noContent().build();

    }


}
