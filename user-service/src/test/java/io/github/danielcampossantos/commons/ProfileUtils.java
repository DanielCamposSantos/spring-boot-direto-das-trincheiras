package io.github.danielcampossantos.commons;

import io.github.danielcampossantos.domain.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileUtils {
    public List<Profile> createNewProfileList() {
        return List.of(
                Profile.builder()
                        .id(1L)
                        .name("Silviodino")
                        .description("Um maluco com o gosto duvidoso sobre muitas coisas.")
                        .build(),
                Profile.builder()
                        .id(2L)
                        .name("CezinhaGamer")
                        .description("Não precisa falar mais nada")
                        .build(),
                Profile.builder()
                        .id(3L)
                        .name("XendGamer")
                        .description("Eu mesmo")
                        .build()
        );
    }

    public Profile newProfileToSave() {
        return Profile.builder()
                .name("Saviolencia")
                .description("Saviola do ódio")
                .build();
    }

    public Profile newProfileSaved() {
        return Profile.builder()
                .id(99L)
                .name("Saviolencia")
                .description("Saviola do ódio")
                .build();
    }


}
