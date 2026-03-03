package io.github.danielcampossantos.commons;

import io.github.danielcampossantos.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeUtils {
    public List<Anime> newAnimeList() {
        var fullMetal = Anime.builder().id(1L).name("Full Metal Brotherhood").build();
        var steinsGate = Anime.builder().id(2L).name("Steins Gate").build();
        var mashle = Anime.builder().id(3L).name("Mashle").build();
        return new ArrayList<>(List.of(fullMetal, steinsGate, mashle));
    }

    public Anime newAnimeToSave() {
        return Anime.builder()
                .name("One Piece")
                .id(99L)
                .build();

    }
}
