package io.github.danielcampossantos.user;

import io.github.danielcampossantos.docs.ApiBadResponses;
import io.github.danielcampossantos.exception.ApiError;
import io.github.danielcampossantos.exception.DefaultErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@Log4j2
@RequiredArgsConstructor
@Tag(name = "User API", description = "User related endpoints")
@SecurityRequirement(name = "basicAuth")
public class UserController {
    private final UserMapper mapper;

    private final UserService service;


    @GetMapping
    @Operation(summary = "Get all users", description = "Get all users available in the system",
            responses = @ApiResponse(
                    description = "List with all users",
                    responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = UserGetResponse.class)))
            ))

    public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request to return user by first name '{}'", name);

        var users = service.findAll(name);

        var response = mapper.toUserGetResponseList(users);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user by id",
            responses = {
                    @ApiResponse(
                            description = "Get user by its id",
                            responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserGetResponse.class))
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DefaultErrorMessage.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to return user by first id '{}'", id);

        var user = service.findByIdOrThrowBadRequestException(id);

        var response = mapper.toUserGetResponse(user);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Saves a new user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User to be saved",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserPostRequest.class))),

            responses = {
                    @ApiResponse(
                            description = "User is saved",
                            responseCode = "201",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserPostResponse.class))
                    ),



            })
    @ApiBadResponses
    public ResponseEntity<UserPostResponse> save(@RequestBody @Valid UserPostRequest userPostRequest) {
        log.debug("Request to save user '{}'", userPostRequest);

        var user = mapper.toUser(userPostRequest);

        var savedUser = service.save(user);

        var response = mapper.toUserPostResponse(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Request to delete user by id '{}'", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequest request) {
        log.debug("Request to update user '{}'", request);

        var userUpdated = mapper.toUser(request);

        service.update(userUpdated);

        return ResponseEntity.noContent().build();
    }

}
