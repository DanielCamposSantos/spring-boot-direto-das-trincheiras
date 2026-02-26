package io.github.danielcampossantos.repository;

import io.github.danielcampossantos.domain.Anime;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {
    @Getter
    private List<Anime> animes;

    {
        var ninjaKamui = Anime.builder().id(1L).name("Ninja Kamui").build();
        var kaijuu = Anime.builder().id(2L).name("Kaijuu-8gou").build();
        var kimetsuNoYaiba = Anime.builder().id(3L).name("Kimetsu No Yaiba").build();
        animes = new ArrayList<>(List.of(ninjaKamui, kaijuu, kimetsuNoYaiba));
    }
}
