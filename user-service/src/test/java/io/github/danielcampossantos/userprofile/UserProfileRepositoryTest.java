package io.github.danielcampossantos.userprofile;

import io.github.danielcampossantos.commons.UserUtils;
import io.github.danielcampossantos.config.TestcontainersConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({UserUtils.class, TestcontainersConfiguration.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository repository;


    @Test
    @DisplayName("findAll returns a list with all users by profile id")
    @Order(1)
    @Sql("/sql/init_user_profile_two_users_one_profile.sql")
    void findAllUsersByProfileId_ReturnsAllUsersByProfileId_WhenSuccessful() {
        var profileId = 1L;
        var users = repository.findAllUsersByProfileId(profileId);

        Assertions.assertThat(users).isNotEmpty()
                .hasSize(2)
                .doesNotContainNull();


        users.forEach(user -> Assertions.assertThat(user).hasNoNullFieldsOrProperties());
    }


}