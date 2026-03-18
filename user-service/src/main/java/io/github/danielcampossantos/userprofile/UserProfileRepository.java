package io.github.danielcampossantos.userprofile;

import io.github.danielcampossantos.domain.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("SELECT up FROM UserProfile up JOIN FETCH up.user u JOIN FETCH up.profile p")
    List<UserProfile> retriveAll();

    @EntityGraph(value = "UserProfile.fullDetails")
    List<UserProfile> findAll();


}
