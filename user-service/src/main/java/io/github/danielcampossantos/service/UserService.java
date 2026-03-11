package io.github.danielcampossantos.service;

import io.github.danielcampossantos.exception.BadRequestException;
import io.github.danielcampossantos.domain.User;
import io.github.danielcampossantos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByFirstNameIgnoreCase(name);
    }

    public User findByIdOrThrowBadRequestException(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void delete(Long id) {
        var userToDelete = findByIdOrThrowBadRequestException(id);
        repository.delete(userToDelete);
    }

    public void update(User userToUpdate) {
        assertUserExists(userToUpdate.getId());
        repository.save(userToUpdate);
    }

    private void assertUserExists(Long id) {
        findByIdOrThrowBadRequestException(id);
    }


}
