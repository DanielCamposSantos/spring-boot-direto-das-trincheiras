package io.github.danielcampossantos.user;

import io.github.danielcampossantos.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  List<User> findByFirstNameIgnoreCase(String name);

  Optional<User> findByEmail(String email);

  Optional<User> findByEmailAndIdNot(String email, Long id);
}
