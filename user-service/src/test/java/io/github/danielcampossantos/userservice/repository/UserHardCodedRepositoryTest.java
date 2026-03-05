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

}