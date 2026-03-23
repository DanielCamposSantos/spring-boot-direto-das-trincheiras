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
                        .name("Admin")
                        .description("Manages everything")
                        .build(),
                Profile.builder()
                        .id(2L)
                        .name("Tester")
                        .description("System Tester")
                        .build()
        );
    }

    public Profile newProfileToSave() {
        return Profile.builder()
                .name("Viewer")
                .description("Regular Profile")
                .build();
    }

    public Profile newProfileSaved() {
        return Profile.builder()
                .id(99L)
                .name("Viewer")
                .description("Regular Profile")
                .build();
    }


}
