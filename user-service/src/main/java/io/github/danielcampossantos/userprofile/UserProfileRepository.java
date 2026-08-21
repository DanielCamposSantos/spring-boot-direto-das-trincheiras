package io.github.danielcampossantos.userprofile;

import io.github.danielcampossantos.domain.User;
import io.github.danielcampossantos.domain.UserProfile;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

  @Query("SELECT up FROM UserProfile up JOIN FETCH up.user u JOIN FETCH up.profile p")
  List<UserProfile> retriveAll();

  @EntityGraph(value = "UserProfile.fullDetails")
  List<UserProfile> findAll();

  @Query("SELECT up.user FROM UserProfile up WHERE up.profile.id = ?1")
  List<User> findAllUsersByProfileId(Long id);

}
