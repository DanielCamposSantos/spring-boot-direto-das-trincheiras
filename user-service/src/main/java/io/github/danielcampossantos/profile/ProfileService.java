package io.github.danielcampossantos.profile;

import io.github.danielcampossantos.domain.Profile;
import io.github.danielcampossantos.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository repository;

    public List<Profile> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByNameIgnoreCase(name);
    }

    public Page<Profile> findAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Profile findByIdOrThrowBadRequestException(Long id) {
        return repository.findById(id).orElseThrow(this::throwBadRequestException);
    }

    public Profile save(Profile profile) {
        return repository.save(profile);
    }

    private BadRequestException throwBadRequestException() {
        return new BadRequestException("Profile not found");
    }

}
