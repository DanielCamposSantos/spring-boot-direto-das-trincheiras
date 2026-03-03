package io.github.danielcampossantos.userservice.repository;

import io.github.danielcampossantos.userservice.domain.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserData {
    @Getter
    private final List<User> users;

    {
        var daniel = User.builder()
                .firstName("Daniel")
                .lastName("Campos")
                .email("danielcampos.santos@gmail.com")
                .id(1L)
                .build();

        var amanda = User.builder()
                .firstName("Amanda")
                .lastName("Campos")
                .email("amandacampos.santos@gmail.com")
                .id(2L)
                .build();

        var miguel = User.builder()
                .firstName("Miguel")
                .lastName("Campos")
                .email("miguelcampos.santos@gmail.com")
                .id(3L)
                .build();
        users = new ArrayList<>(List.of(daniel, amanda, miguel));
    }
}
