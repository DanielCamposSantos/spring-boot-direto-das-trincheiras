package io.github.danielcampossantos.profile;

import io.github.danielcampossantos.domain.Profile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

  Optional<Profile> findByNameIgnoreCase(String name);


}
