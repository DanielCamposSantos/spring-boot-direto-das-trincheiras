package io.github.danielcampossantos.userprofile;

import io.github.danielcampossantos.domain.User;
import io.github.danielcampossantos.domain.UserProfile;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

  private final UserProfileRepository repository;

  public List<UserProfile> findAll() {
    return repository.findAll();
  }

  public List<User> findAllUsersByProfileId(Long id) {
    return repository.findAllUsersByProfileId(id);
  }

}
