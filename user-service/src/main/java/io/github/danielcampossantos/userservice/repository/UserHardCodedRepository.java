package io.github.danielcampossantos.userservice.repository;

import io.github.danielcampossantos.userservice.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserHardCodedRepository {
    private final UserData userData;

    public List<User> findAll() {
        return userData.getUsers();
    }

    public List<User> findByName(String name) {
        return userData.getUsers().stream()
                .filter(user -> user.getFirstName().equals(name))
                .toList();
    }

    public Optional<User> findById(Long id) {
        return userData.getUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

}
