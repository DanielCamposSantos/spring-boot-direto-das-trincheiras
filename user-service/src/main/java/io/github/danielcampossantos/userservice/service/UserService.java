package io.github.danielcampossantos.userservice.service;

import io.github.danielcampossantos.userservice.domain.User;
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
}
