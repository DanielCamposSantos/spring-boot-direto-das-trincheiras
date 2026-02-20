package io.github.danielcampossantos.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/heroes")
public class HeroesController {
    private static final List<String> HEROES = List.of("Guts","Zoro","Kakashi","Goku");

    @GetMapping
    public List<String> listAllHeroes(){
        return HEROES;
    }
    @GetMapping("filter")
    public List<String> listAllHeroesParam(@RequestParam String name){
        return HEROES.stream().filter(heroe -> heroe.equalsIgnoreCase(name)).toList();
    }

    @GetMapping("filterList")
    public List<String> listAllHeroesParam(@RequestParam(defaultValue = "") List<String> names){
        return HEROES.stream().filter(names::contains).toList();
    }

    @GetMapping("{name}")
    public String findByname(@PathVariable String name){
        return HEROES.stream().filter(heroe -> heroe.equalsIgnoreCase(name)).collect(Collectors.joining());
    }
}
