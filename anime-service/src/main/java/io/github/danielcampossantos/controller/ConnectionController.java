package io.github.danielcampossantos.controller;

import io.github.danielcampossantos.config.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "connections")
@Log4j2
@RequiredArgsConstructor
public class ConnectionController {
    private final Connection connection;


    @GetMapping
    public ResponseEntity<Connection> getConnection() {
        return ResponseEntity.ok(connection);
    }
}
