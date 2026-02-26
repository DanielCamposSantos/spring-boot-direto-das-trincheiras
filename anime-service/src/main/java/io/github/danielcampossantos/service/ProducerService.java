package io.github.danielcampossantos.service;

import io.github.danielcampossantos.domain.Producer;
import io.github.danielcampossantos.repository.ProducerHardCodedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProducerService {

    private final ProducerHardCodedRepository repository;

    public List<Producer> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Producer findByIdOrThrowBadRequest(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "producer not found"));
    }

    public Producer save(Producer producer) {
        return repository.save(producer);
    }


    public void delete(Long id) {
        var producer = findByIdOrThrowBadRequest(id);
        repository.delete(producer);
    }

    public void update(Producer producerToUpdate) {
        var producer = findByIdOrThrowBadRequest(producerToUpdate.getId());
        producer.setCreatedAt(producerToUpdate.getCreatedAt());
        repository.update(producer);
    }


}
