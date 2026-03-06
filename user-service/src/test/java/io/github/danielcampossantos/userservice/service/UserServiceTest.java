package io.github.danielcampossantos.userservice.service;

import io.github.danielcampossantos.userservice.commons.UserUtils;
import io.github.danielcampossantos.userservice.domain.User;
import io.github.danielcampossantos.userservice.repository.UserHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    @DisplayName("findAll returns list user found by name when successful")
    void findAll_ReturnsListWithUserFoundByName_WhenNameIsNull() {
        var userToBeFound = userList.getFirst();
        var expectedUserFound = Collections.singletonList(userToBeFound);

        BDDMockito.when(repository.findByName(userToBeFound.getFirstName())).thenReturn(expectedUserFound);

        var users = service.findAll(userToBeFound.getFirstName());

        Assertions.assertThat(users)
                .isNotEmpty()
                .isNotNull()
                .contains(userToBeFound)
                .isEqualTo(expectedUserFound);
    }

    @Test
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
    @DisplayName("findById returns user by id when successful")
    void findById_ReturnsUserById_WhenSuccessful() {
        var userToBeFound = userList.getFirst();
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(userToBeFound));

        var user = service.findByIdOrThrowResponseStatusException(userToBeFound.getId());

        Assertions.assertThat(user.getId())
                .isNotNull()
                .isEqualTo(userToBeFound.getId());

        Assertions.assertThat(user.getFirstName()).isEqualTo(userToBeFound.getFirstName());
    }

    @Test
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

//
//    @Test
//    @DisplayName("save saves user when successful")
//    void save_SavesUser_WhenSuccessful() {
//        BDDMockito.when(userData.getUsers()).thenReturn(userList);
//
//        var userToBeSaved = userUtils.newUserToBeSaved();
//
//        var user = repository.save(userToBeSaved);
//
//        Assertions.assertThat(user)
//                .isNotNull()
//                .isEqualTo(userToBeSaved);
//
//        Assertions.assertThat(userData.getUsers())
//                .isNotEmpty()
//                .contains(userToBeSaved);
//    }
//
//    @Test
//    @DisplayName("delete removes user when successful")
//    void delete_RemovesUser_WhenSuccessful() {
//        BDDMockito.when(userData.getUsers()).thenReturn(userList);
//
//        var userToBeDeleted = userData.getUsers().getFirst();
//
//        repository.delete(userToBeDeleted);
//
//        Assertions.assertThat(userData.getUsers())
//                .isNotEmpty()
//                .doesNotContain(userToBeDeleted);
//    }
//
//    @Test
//    @DisplayName("udpate udpates user when successful")
//    void udpate_UpdatesUser_WhenSuccessful() {
//        BDDMockito.when(userData.getUsers()).thenReturn(userList);
//
//        var userToBeUpdated = userData.getUsers().getFirst();
//
//        userToBeUpdated = userUtils.newUserToBeUpdated(userToBeUpdated.getId());
//
//        repository.update(userToBeUpdated);
//
//        Assertions.assertThat(userData.getUsers())
//                .isNotEmpty()
//                .contains(userToBeUpdated);
//
//        var userOptional = repository.findById(userToBeUpdated.getId());
//
//        Assertions.assertThat(userOptional).isPresent();
//        Assertions.assertThat(userOptional.get().getId()).isEqualTo(userToBeUpdated.getId());
//    }
//


}