package io.github.danielcampossantos.userprofile;

import io.github.danielcampossantos.commons.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({UserUtils.class})
class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository repository;

    @Test
    @DisplayName("findAll returns list with all users by profile id")
    @Sql("/sql/init_user_profile_2_users_1_profile.sql")
    void findAll_ReturnsListWithAllUsersByProfileId_WhenSuccessful() {
        var profileId = 1L;
        var users = repository.findAllUsersByProfileId(profileId);

        Assertions.assertThat(users)
                .isNotEmpty()
                .doesNotContainNull()
                .hasSize(2);

        users.forEach(user -> Assertions.assertThat(user).hasNoNullFieldsOrProperties());

    }


}