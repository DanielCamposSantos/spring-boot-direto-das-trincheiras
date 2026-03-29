package io.github.danielcampossantos.commons;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.nio.file.Files;

@Component
@RequiredArgsConstructor
public class FileUtils {
    private final ResourceLoader resourceLoader;

    @SneakyThrows
    public String readResourceFile(String fileName) {
        var file = resourceLoader.getResource("classpath:" + fileName).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }

}
