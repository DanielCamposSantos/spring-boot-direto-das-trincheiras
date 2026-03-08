package io.github.danielcampossantos.userservice.service;

import io.github.danielcampossantos.userservice.domain.User;
import io.github.danielcampossantos.userservice.exception.BadRequestException;
import io.github.danielcampossantos.userservice.repository.UserHardCodedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserHardCodedRepository repository;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
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
        repository.update(userToUpdate);
    }

    private void assertUserExists(Long id) {
        findByIdOrThrowBadRequestException(id);
    }


}
