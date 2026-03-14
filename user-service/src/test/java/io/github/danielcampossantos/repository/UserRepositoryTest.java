package io.github.danielcampossantos.repository;

import io.github.danielcampossantos.commons.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import(UserUtils.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserUtils userUtils;


    @Test
    @Order(1)
    @DisplayName("save creates user when successful")
    void save_CreatesUser_WhenSuccessful() {
        var userToSave = userUtils.newUserToSave().withId(null);

        var savedUser = repository.save(userToSave);

        Assertions.assertThat(savedUser)
                .hasNoNullFieldsOrProperties();

        Assertions.assertThat(savedUser.getId()).isEqualTo(1L);
    }

    @Test
    @Order(2)
    @DisplayName("findAll returns list with all users when successful")
    @Sql("/sql/init_one_user.sql")
    void findAll_ReturnsListWithAllUsers_WhenSuccessful() {
        var users = repository.findAll();

        Assertions.assertThat(users)
                .isNotEmpty();
    }
}