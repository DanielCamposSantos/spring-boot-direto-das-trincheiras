package io.github.danielcampossantos.anime;

import io.github.danielcampossantos.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    List<Anime> findByNameIgnoreCase(String name);
}
