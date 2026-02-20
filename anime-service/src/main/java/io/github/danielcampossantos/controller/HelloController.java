package io.github.danielcampossantos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping
    public String hi() {
        return "OMAE WA MO SHINDE IRU";
    }
}
