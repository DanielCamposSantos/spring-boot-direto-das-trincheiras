package io.github.danielcampossantos.user;

import io.github.danielcampossantos.annotation.EncodedMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderMapper {
    private final PasswordEncoder passwordEncoder;

    @EncodedMapping
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
