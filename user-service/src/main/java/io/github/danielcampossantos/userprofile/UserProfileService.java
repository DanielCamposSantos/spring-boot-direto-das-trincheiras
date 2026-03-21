package io.github.danielcampossantos.userprofile;

import io.github.danielcampossantos.domain.User;
import io.github.danielcampossantos.domain.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
