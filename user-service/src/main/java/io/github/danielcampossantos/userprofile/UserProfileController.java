package io.github.danielcampossantos.userprofile;

import io.github.danielcampossantos.domain.UserProfile;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user-profiles")
@Log4j2
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService service;



    @GetMapping
    public ResponseEntity<List<UserProfile>> findAll(){
        log.debug("Request to get all user profiles");

        var userProfiles = service.findAll();

        return ResponseEntity.ok(userProfiles);
    }
}
