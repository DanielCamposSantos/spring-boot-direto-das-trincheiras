package io.github.danielcampossantos.userprofile;

import io.github.danielcampossantos.commons.ProfileUtils;
import io.github.danielcampossantos.commons.UserProfileUtils;
import io.github.danielcampossantos.commons.UserUtils;
import io.github.danielcampossantos.domain.UserProfile;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserProfileServiceTest {
    @InjectMocks
    private UserProfileService service;

    @Mock
    private UserProfileRepository repository;

    @InjectMocks
    private UserProfileUtils userProfileUtils;

    @Spy
    private UserUtils userUtils;

    @Spy
    private ProfileUtils profileUtils;

    private static List<UserProfile> userProfileList;

    @BeforeEach
    void init() {
        userProfileList = userProfileUtils.newUserProfileList();
    }


    @Test
    @Order(1)
    @DisplayName("findAll returns list with all user profiles")
    void findAll_ReturnsListWithAllUserProfiles_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(userProfileList);

        var userProfiles = service.findAll();

        Assertions.assertThat(userProfiles)
                .isNotNull()
                .hasSameElementsAs(userProfileList);

        userProfiles.forEach(userProfile -> Assertions.assertThat(userProfile).hasNoNullFieldsOrProperties());
    }

    @Test
    @Order(2)
    @DisplayName("findAllUsersByProfileId returns list with users by profile id")
    void findAllUsersByProfileId_ReturnsListWithUsersByProfileId_WhenSuccessful() {
        var profileId = 99L;

        var usersToFind = userProfileList.stream()
                .filter(u -> u.getProfile().getId().equals(profileId))
                .map(UserProfile::getUser)
                .toList();

        BDDMockito.when(repository.findAllUsersByProfileId(profileId)).thenReturn(usersToFind);

        var users = service.findAllUsersByProfileId(profileId);

        Assertions.assertThat(users)
                .isNotNull()
                .hasSameElementsAs(usersToFind);

        users.forEach(user -> Assertions.assertThat(user).hasNoNullFieldsOrProperties());
    }

}