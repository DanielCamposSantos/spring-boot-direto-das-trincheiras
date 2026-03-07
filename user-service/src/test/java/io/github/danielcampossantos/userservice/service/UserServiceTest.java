package io.github.danielcampossantos.userservice.service;

import io.github.danielcampossantos.userservice.commons.UserUtils;
import io.github.danielcampossantos.userservice.domain.User;
import io.github.danielcampossantos.userservice.repository.UserHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserHardCodedRepository repository;

    @InjectMocks
    private UserUtils userUtils;

    private static List<User> userList;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @Order(1)
    @DisplayName("findAll returns list user found by name when successful")
    void findAll_ReturnsListWithUserFoundByName_WhenNameIsNull() {
        var userToFound = userList.getFirst();
        var expectedUserFound = Collections.singletonList(userToFound);

        BDDMockito.when(repository.findByName(userToFound.getFirstName())).thenReturn(expectedUserFound);

        var users = service.findAll(userToFound.getFirstName());

        Assertions.assertThat(users)
                .isNotEmpty()
                .isNotNull()
                .contains(userToFound)
                .isEqualTo(expectedUserFound);
    }

    @Test
    @Order(2)
    @DisplayName("findAll returns list with all users when successful")
    void findAll_ReturnsListWithAllUsers_WhenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var users = service.findAll(null);

        Assertions.assertThat(repository.findAll())
                .isNotEmpty()
                .isNotNull()
                .isEqualTo(users);

    }

    @Test
    @Order(3)
    @DisplayName("findAll returns empty list when user not found")
    void findAll_ReturnsEmptyList_WhenUserNotFound() {
        var userNotFound = "not-found";

        BDDMockito.when(repository.findByName(userNotFound)).thenReturn(Collections.emptyList());

        var users = service.findAll(userNotFound);

        Assertions.assertThat(users)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("findById returns user by id when successful")
    void findById_ReturnsUserById_WhenSuccessful() {
        var userToFound = userList.getFirst();
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(userToFound));

        var user = service.findByIdOrThrowResponseStatusException(userToFound.getId());

        Assertions.assertThat(user.getId())
                .isNotNull()
                .isEqualTo(userToFound.getId());

        Assertions.assertThat(user.getFirstName()).isEqualTo(userToFound.getFirstName());
    }

    @Test
    @Order(5)
    @DisplayName("findById throws ResponseStatusException")
    void findById_ThrowsResponseStatusException_WhenUserNotFoundById() {
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        var id = 99L;

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowResponseStatusException(id))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    @Order(6)
    @DisplayName("save creates user when successful")
    void save_CreatesUser_WhenSuccessful() {
        var userToSaved = userUtils.newUserToSave();

        BDDMockito.when(repository.save(userToSaved)).thenReturn(userToSaved);

        var user = service.save(userToSaved);

        Assertions.assertThat(user)
                .isNotNull()
                .isEqualTo(userToSaved);
    }

    @Test
    @Order(7)
    @DisplayName("delete removes user when successful")
    void delete_RemovesUser_WhenSuccessful() {
        var userToDelete = userList.getFirst();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(userToDelete));

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToDelete.getId()));
    }

    @Test
    @Order(8)
    @DisplayName("delete throws ResponseStatusException")
    void delete_ThrowsResponseStatusException_WhenUserNotFound() {
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        var id = 99L;

        Assertions.assertThatException().isThrownBy(() -> service.delete(id));
    }


    @Test
    @Order(9)
    @DisplayName("udpate udpates user when successful")
    void udpate_UpdatesUser_WhenSuccessful() {

        var userToUpdate = userUtils.newUserToUpdate(userList.getFirst().getId());

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(userToUpdate));

        Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));

    }

    @Test
    @Order(10)
    @DisplayName("udpate throws ResponseStatusException")
    void udpate_ThrowsResponseStatusException_WhenUserNotFound() {

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        var notFoundId = 99L;

        var userToUpdate = userUtils.newUserToUpdate(notFoundId);

        Assertions.assertThatException().isThrownBy(() -> service.update(userToUpdate));

    }


}