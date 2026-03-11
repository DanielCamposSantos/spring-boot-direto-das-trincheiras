package io.github.danielcampossantos.service;

import io.github.danielcampossantos.exception.BadRequestException;
import io.github.danielcampossantos.commons.UserUtils;
import io.github.danielcampossantos.domain.User;
import io.github.danielcampossantos.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;


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

        BDDMockito.when(repository.findByFirstNameIgnoreCase(userToFound.getFirstName())).thenReturn(expectedUserFound);

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

        Assertions.assertThat(users)
                .isNotEmpty()
                .hasSameElementsAs(userList);
    }

    @Test
    @Order(3)
    @DisplayName("findAll returns empty list when user not found")
    void findAll_ReturnsEmptyList_WhenUserNotFound() {
        var userNotFound = "not-found";

        BDDMockito.when(repository.findByFirstNameIgnoreCase(userNotFound)).thenReturn(Collections.emptyList());

        var users = service.findAll(userNotFound);

        Assertions.assertThat(users)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("findById returns user by id when successful")
    void findById_ReturnsUserById_WhenSuccessful() {
        var userToFind = userList.getFirst();
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(userToFind));

        var user = service.findByIdOrThrowBadRequestException(userToFind.getId());

        Assertions.assertThat(user.getId())
                .isNotNull()
                .isEqualTo(userToFind.getId());

        Assertions.assertThat(user.getFirstName()).isEqualTo(userToFind.getFirstName());
    }

    @Test
    @Order(5)
    @DisplayName("findById throws BadRequestException")
    void findById_ThrowsBadRequestException_WhenUserNotFoundById() {
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        var id = 99L;

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowBadRequestException(id))
                .isInstanceOf(BadRequestException.class)
                .extracting(e -> ((BadRequestException) e).getStatusCode())
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
    @DisplayName("delete throws BadRequestException")
    void delete_ThrowsBadRequestException_WhenUserNotFound() {
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        var id = 99L;

        Assertions.assertThatException().isThrownBy(() -> service.delete(id));
    }


    @Test
    @Order(9)
    @DisplayName("update updates user when successful")
    void update_UpdatesUser_WhenSuccessful() {

        var userToUpdate = userUtils.newUserToUpdate(userList.getFirst().getId());

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(userToUpdate));

        Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));

    }

    @Test
    @Order(10)
    @DisplayName("update throws BadRequestException")
    void update_ThrowsBadRequestException_WhenUserNotFound() {

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        var notFoundId = 99L;

        var userToUpdate = userUtils.newUserToUpdate(notFoundId);

        Assertions.assertThatException().isThrownBy(() -> service.update(userToUpdate));

    }


}