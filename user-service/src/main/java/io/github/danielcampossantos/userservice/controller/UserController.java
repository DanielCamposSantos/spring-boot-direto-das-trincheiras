package io.github.danielcampossantos.userservice.controller;

import io.github.danielcampossantos.userservice.mapper.UserMapper;
import io.github.danielcampossantos.userservice.response.UserGetResponse;
import io.github.danielcampossantos.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
