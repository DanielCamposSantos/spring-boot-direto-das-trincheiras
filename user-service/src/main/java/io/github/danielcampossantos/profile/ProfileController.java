package io.github.danielcampossantos.profile;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profiles")
@Tag(name = "Profiles API", description = "Profile related endpoints")
@SecurityRequirement(name = "basicAuth")
public class ProfileController {

  private final ProfileService service;
  private final ProfileMapper mapper;

  @GetMapping
  public ResponseEntity<List<ProfileGetResponse>> findAll() {
    var profiles = service.findAll();

    var response = mapper.toProfileGetResponseList(profiles);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProfileGetResponse> findAll(@PathVariable Long id) {
    var profile = service.findByIdOrThrowBadRequestException(id);
    var response = mapper.toProfileGetResponse(profile);

    return ResponseEntity.ok(response);
  }

  @GetMapping(params = "name")
  public ResponseEntity<ProfileGetResponse> findByName(@RequestParam String name) {
    var profiles = service.findByNameOrThrowBadRequestException(name);

    var response = mapper.toProfileGetResponse(profiles);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/paginated")
  public ResponseEntity<Page<ProfileGetResponse>> findAllPaginated(Pageable pageable) {
    var paginatedProfiles = service.findAllPaginated(pageable).map(mapper::toProfileGetResponse);

    return ResponseEntity.ok(paginatedProfiles);
  }


  @PostMapping
  public ResponseEntity<ProfilePostResponse> save(@RequestBody @Valid ProfilePostRequest request) {
    var profile = mapper.toProfile(request);
    var savedProfile = service.save(profile);
    var response = mapper.toProfilePostResponse(savedProfile);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);

  }


}
