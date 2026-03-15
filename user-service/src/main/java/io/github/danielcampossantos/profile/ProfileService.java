package io.github.danielcampossantos.profile;

import io.github.danielcampossantos.domain.Profile;
import io.github.danielcampossantos.exception.BadRequestException;
import io.github.danielcampossantos.exception.ProfileAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository repository;

    public List<Profile> findAll() {
        return repository.findAll();
    }

    public Profile findByNameOrThrowBadRequestException(String name) {
        return repository.findByNameIgnoreCase(name).orElseThrow(this::throwBadRequestException);
    }

    public Page<Profile> findAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Profile findByIdOrThrowBadRequestException(Long id) {
        return repository.findById(id).orElseThrow(this::throwBadRequestException);
    }

    public Profile save(Profile profile) {
        assertProfileDoesNotExistsException(profile.getName());
        return repository.save(profile);
    }

    private void assertProfileDoesNotExistsException(String name) {
        repository.findByNameIgnoreCase(name).ifPresent(this::throwProfileAlreadyExistsException);
    }

    private void throwProfileAlreadyExistsException(Profile profile) {
        throw new ProfileAlreadyExistsException("Profile %s already exists".formatted(profile.getName()));
    }


    private BadRequestException throwBadRequestException() {
        return new BadRequestException("Profile not found");
    }

}
