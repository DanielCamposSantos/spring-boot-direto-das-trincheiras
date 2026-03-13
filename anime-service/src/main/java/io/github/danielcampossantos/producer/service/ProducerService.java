package io.github.danielcampossantos.producer.service;

import io.github.danielcampossantos.producer.repository.ProducerRepository;
import io.github.danielcampossantos.domain.Producer;
import io.github.danielcampossantos.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ProducerRepository repository;

    public List<Producer> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByNameIgnoreCase(name);
    }

    public Producer findByIdOrThrowBadRequest(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Producer not found"));
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
        repository.save(producer);
    }


}
