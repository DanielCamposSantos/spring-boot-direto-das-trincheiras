package io.github.danielcampossantos.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class Anime {
    private Long id;
    private String name;
    @Getter
    private static List<Anime> animes = new ArrayList<>();

    static {
        var ninjaKamui =Anime.builder().id(1L).name("Ninja Kamui").build();
        var kaijuu =Anime.builder().id(2L).name("Kaijuu-8gou").build();
        var kimetsuNoYaiba =Anime.builder().id(3L).name("Kimetsu No Yaiba").build();
        animes.addAll(List.of(ninjaKamui,kaijuu,kimetsuNoYaiba));
    }

}
