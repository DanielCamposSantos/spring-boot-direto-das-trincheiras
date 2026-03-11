package io.github.danielcampossantos.controller;

import io.github.danielcampossantos.mapper.UserMapper;
import io.github.danielcampossantos.request.UserPostRequest;
import io.github.danielcampossantos.request.UserPutRequest;
import io.github.danielcampossantos.response.UserGetResponse;
import io.github.danielcampossantos.response.UserPostResponse;
import io.github.danielcampossantos.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@Log4j2
@RequiredArgsConstructor
public class UserController {
    private final UserMapper mapper;

    private final UserService service;


    @GetMapping
    public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request to return user by first name '{}'", name);

        var users = service.findAll(name);

        var response = mapper.toUserGetResponseList(users);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to return user by first id '{}'", id);

        var user = service.findByIdOrThrowBadRequestException(id);

        var response = mapper.toUserGetResponse(user);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserPostResponse> save(@RequestBody @Valid UserPostRequest userPostRequest) {
        log.debug("Request to save user '{}'", userPostRequest);

        var user = mapper.toUser(userPostRequest);

        var savedUser = service.save(user);

        var response = mapper.toUserPostResponse(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Request to delete user by id '{}'", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequest request) {
        log.debug("Request to update user '{}'", request);

        var userUpdated = mapper.toUser(request);

        service.update(userUpdated);

        return ResponseEntity.noContent().build();
    }

}
