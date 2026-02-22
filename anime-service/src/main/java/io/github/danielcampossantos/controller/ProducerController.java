package io.github.danielcampossantos.controller;


import io.github.danielcampossantos.domain.Producer;
import io.github.danielcampossantos.mapper.ProducerMapper;
import io.github.danielcampossantos.requests.ProducerPostRequest;
import io.github.danielcampossantos.response.ProducerGetResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("producers")
public class ProducerController {

    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> listAll(@RequestParam(required = false) String name) {
        log.debug("Request to get list of producers by name '{}'", name);
        var producers = Producer.getProducers();

        var producerGetResponseList = MAPPER.toProducerGetResponseList(producers);

        if (name == null) return ResponseEntity.ok(producerGetResponseList);

        var response = producerGetResponseList.stream()
                .filter(producer -> producer.name().equalsIgnoreCase(name))
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ProducerGetResponse> getById(@PathVariable long id) {
        log.debug("Request to get producer by id '{}'", id);
        var producerGetResponse = Producer.getProducers().stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst()
                .map(MAPPER::toProducerGetResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "producer not found"));

        return ResponseEntity.ok(producerGetResponse);

    }


    @PostMapping
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest) {
        log.debug("Saving producer '{}'", producerPostRequest);

        var producer = MAPPER.toProducer(producerPostRequest);

        Producer.getProducers().add(producer);

        var response = MAPPER.toProducerGetResponse(producer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        log.debug("Request to delete producer by id '{}'", id);

        var producerToDelete = Producer.getProducers().stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "producer not found"));

        Producer.getProducers().remove(producerToDelete);

        return ResponseEntity.noContent().build();
    }


}
