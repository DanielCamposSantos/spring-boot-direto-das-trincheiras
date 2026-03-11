package io.github.danielcampossantos.commons;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;

@Component
@RequiredArgsConstructor
public class FileUtils {
    private final ResourceLoader resourceLoader;

    public String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:" + fileName).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }

}
