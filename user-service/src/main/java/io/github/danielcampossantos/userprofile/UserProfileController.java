package io.github.danielcampossantos.userprofile;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user-profiles")
@Log4j2
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class UserProfileController {

    private final UserProfileService service;
    private final UserProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserProfileGetResponse>> findAll() {
        log.debug("Request to get all user profiles");

        var userProfiles = service.findAll();

        var response = mapper.toUserProfileGetResponseList(userProfiles);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profiles/{id}/users")
    public ResponseEntity<List<UserProfileUserGetResponse>> findAllUsersByProfileId(@PathVariable Long id) {
        log.debug("Request to get all user profiles by id {}", id);

        var users = service.findAllUsersByProfileId(id);

        var response = mapper.toUserProfileUsersGetResponseList(users);

        return ResponseEntity.ok(response);
    }
}
