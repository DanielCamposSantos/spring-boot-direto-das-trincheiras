package io.github.danielcampossantos.service;

import io.github.danielcampossantos.domain.User;
import io.github.danielcampossantos.exception.BadRequestException;
import io.github.danielcampossantos.exception.EmailAlreadyExistsException;
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
        assertThatEmailDoesNotExist(user.getEmail());
        return repository.save(user);
    }

    public void delete(Long id) {
        var userToDelete = findByIdOrThrowBadRequestException(id);
        repository.delete(userToDelete);
    }

    public void update(User userToUpdate) {
        assertUserExists(userToUpdate.getId());
        assertThatEmailDoesNotExist(userToUpdate.getEmail(), userToUpdate.getId());
        repository.save(userToUpdate);
    }

    private void assertUserExists(Long id) {
        findByIdOrThrowBadRequestException(id);
    }

    private void assertThatEmailDoesNotExist(String email) {
        repository.findByEmail(email).ifPresent(this::throwEmailAlreadyExistsException);
    }

    private void assertThatEmailDoesNotExist(String email, Long id) {
        repository.findByEmailAndIdNot(email, id).ifPresent(this::throwEmailAlreadyExistsException);
    }

    private void throwEmailAlreadyExistsException(User user) {
        throw new EmailAlreadyExistsException("Email %s already exists".formatted(user.getEmail()));
    }
}
