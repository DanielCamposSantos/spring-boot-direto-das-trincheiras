package io.github.danielcampossantos.userservice.repository;

import io.github.danielcampossantos.userservice.commons.UserUtils;
import io.github.danielcampossantos.userservice.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserHardCodedRepositoryTest {

    @InjectMocks
    private UserHardCodedRepository repository;

    @Mock
    private UserData userData;

    @InjectMocks
    private UserUtils userUtils;

    private static List<User> userList;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll returns list with all users when successful")
    void findAll_ReturnsListWithAllUsers_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        Assertions.assertThat(repository.findAll())
                .isNotEmpty()
                .isNotNull()
                .isEqualTo(userList);

    }

    @Test
    @DisplayName("findByName returns list user found by name when successful ")
    void findByName_ReturnsListUserFoundByName_WhenSuccessfull() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToBeFound = userData.getUsers().getFirst();

        Assertions.assertThat(repository.findByName(userToBeFound.getFirstName()))
                .isNotEmpty()
                .isNotNull()
                .contains(userToBeFound);
    }

    @Test
    @DisplayName("findByName returns empty list when name is not found")
    void findByName_ReturnsEmptyList_WhenNameIsNotFound() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var nameNotFound = "name-not-found";
        Assertions.assertThat(repository.findByName(nameNotFound))
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findById returns user by id when successful")
    void findById_ReturnsUserById_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToBeFound = userData.getUsers().getFirst();

        Optional<User> user = repository.findById(userToBeFound.getId());

        Assertions.assertThat(user).isPresent();

        Assertions.assertThat(user.get().getId())
                .isNotNull()
                .isEqualTo(userToBeFound.getId());

        Assertions.assertThat(user.get().getFirstName()).isEqualTo(userToBeFound.getFirstName());
    }

    @Test
    @DisplayName("findById returns empty when user not found")
    void findById_ReturnsEmpty_WhenUserNotFound() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var id = 99L;

        var userOptional = repository.findById(id);

        Assertions.assertThat(userOptional).isNotPresent();

    }

    @Test
    @DisplayName("save saves user when successful")
    void save_SavesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToBeSaved = userUtils.newUserToSave();

        var user = repository.save(userToBeSaved);

        Assertions.assertThat(user)
                .isNotNull()
                .isEqualTo(userToBeSaved);

        Assertions.assertThat(userData.getUsers())
                .isNotEmpty()
                .contains(userToBeSaved);
    }

    @Test
    @DisplayName("delete removes user when successful")
    void delete_RemovesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToBeDeleted = userData.getUsers().getFirst();

        repository.delete(userToBeDeleted);

        Assertions.assertThat(userData.getUsers())
                .isNotEmpty()
                .doesNotContain(userToBeDeleted);
    }

    @Test
    @DisplayName("udpate udpates user when successful")
    void udpate_UpdatesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToBeUpdated = userData.getUsers().getFirst();

        userToBeUpdated = userUtils.newUserToUpdate(userToBeUpdated.getId());

        repository.update(userToBeUpdated);

        Assertions.assertThat(userData.getUsers())
                .isNotEmpty()
                .contains(userToBeUpdated);

        var userOptional = repository.findById(userToBeUpdated.getId());

        Assertions.assertThat(userOptional).isPresent();
        Assertions.assertThat(userOptional.get().getId()).isEqualTo(userToBeUpdated.getId());
    }


}