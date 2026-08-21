package io.github.danielcampossantos.producer;

import io.github.danielcampossantos.domain.Producer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {

  List<Producer> findByNameIgnoreCase(String name);
}
