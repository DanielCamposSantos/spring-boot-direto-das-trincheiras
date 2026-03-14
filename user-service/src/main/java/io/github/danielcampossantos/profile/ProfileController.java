package io.github.danielcampossantos.profile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileService service;
    private final ProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<ProfileGetResponse>> findAll(@RequestParam(required = false) String name) {
        var profiles = service.findAll(name);

        var response = mapper.toProfileGetResponseList(profiles);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<ProfileGetResponse>> findAllPaginated(Pageable pageable) {
        var paginatedProfiles = service.findAllPaginated(pageable).map(mapper::toProfileGetResponse);
        return ResponseEntity.ok(paginatedProfiles);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProfileGetResponse> findAll(@PathVariable Long id) {
        var profile = service.findByIdOrThrowBadRequestException(id);

        var response = mapper.toProfileGetResponse(profile);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProfilePostResponse> save(@RequestBody @Valid ProfilePostRequest request) {
        var profile = mapper.toProfile(request);
        var savedProfile = service.save(profile);
        var response = mapper.toProfilePostResponse(savedProfile);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }


}
