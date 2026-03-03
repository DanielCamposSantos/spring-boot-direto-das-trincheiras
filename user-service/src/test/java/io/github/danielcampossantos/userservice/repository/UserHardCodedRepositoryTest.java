package io.github.danielcampossantos.userservice.repository;

import io.github.danielcampossantos.userservice.commons.UserUtils;
import io.github.danielcampossantos.userservice.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    void findAll_ReturnsListWithAllUsers_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        Assertions.assertThat(repository.findAll())
                .isNotEmpty()
                .isNotNull()
                .isEqualTo(userList);

    }

}