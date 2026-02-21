package io.github.danielcampossantos.controller;

import io.github.danielcampossantos.domain.Anime;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("animes")
public class AnimeController {

    @GetMapping
    public List<Anime> listAll(@RequestParam(required = false) String name){
        var animes = Anime.getAnimes();

        if (name == null) return animes;

        return animes.stream()
                .filter(anime -> anime.getName().equalsIgnoreCase(name))
                .toList();
    }

    @GetMapping("{id}")
    public Anime getById(@PathVariable long id){
        return Anime.getAnimes().stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst()
                .orElse(null);

    }

    @PostMapping
    public Anime save(@RequestBody Anime anime){
        anime.setId(ThreadLocalRandom.current().nextLong(1,1000));
        Anime.getAnimes().add(anime);
        return anime;
    }



}
