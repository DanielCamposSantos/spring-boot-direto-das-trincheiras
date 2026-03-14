package io.github.danielcampossantos.commons;

import io.github.danielcampossantos.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {
    public List<User> newUserList() {
        var cezar = User.builder()
                .firstName("Cezar")
                .lastName("Augusto")
                .email("cezaraugusto.santos@gmail.com")
                .id(1L)
                .build();

        var silvio = User.builder()
                .firstName("Silvio")
                .lastName("Gustavo")
                .email("silviogustavo.santos@gmail.com")
                .id(2L)
                .build();

        var savio = User.builder()
                .firstName("Savio")
                .lastName("Machado")
                .email("saviomachado.santos@gmail.com")
                .id(3L)
                .build();
        return new ArrayList<>(List.of(cezar, silvio, savio));

    }

    public User newUserToSave() {
        return User.builder()
                .id(99L)
                .firstName("Paulo")
                .lastName("Alcantara")
                .email("pauloAlcantara@email.com")
                .build();
    }

}
