package io.github.danielcampossantos.controller;


import io.github.danielcampossantos.domain.Producer;
import io.github.danielcampossantos.requests.ProducerPostRequest;
import io.github.danielcampossantos.response.ProducerGetResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@RestController
@RequestMapping("producers")
public class ProducerController {

    @GetMapping
    public List<Producer> listAll(@RequestParam(required = false) String name) {
        var producers = Producer.getProducers();

        if (name == null) return producers;

        return producers.stream()
                .filter(producer -> producer.getName().equalsIgnoreCase(name))
                .toList();
    }

    @GetMapping(path = "{id}")
    public Producer getById(@PathVariable long id) {
        return Producer.getProducers().stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst()
                .orElse(null);

    }


    @PostMapping
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("{}", headers);

        var producer = Producer.builder()
                .id(ThreadLocalRandom.current().nextLong(1, 100_000))
                .name(producerPostRequest.name())
                .createdAt(LocalDateTime.now())
                .build();

        Producer.getProducers().add(producer);

        var response = ProducerGetResponse.builder()
                .id(producer.getId())
                .name(producer.getName())
                .createdAt(producer.getCreatedAt())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
