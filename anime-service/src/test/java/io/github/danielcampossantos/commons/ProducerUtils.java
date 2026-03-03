package io.github.danielcampossantos.commons;

import io.github.danielcampossantos.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerUtils {
    public List<Producer> newProducerList() {
        var dateTime = "2026-02-27T15:25:51.7839578";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, formatter);


        var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build();
        var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(localDateTime).build();
        var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(localDateTime).build();
        return new ArrayList<>(List.of(ufotable, witStudio, studioGhibli));
    }

    public Producer newProducerToSave() {
        return Producer.builder()
                .name("MAPPA")
                .id(99L)
                .createdAt(LocalDateTime.now())
                .build();

    }
}
