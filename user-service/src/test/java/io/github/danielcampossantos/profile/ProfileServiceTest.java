package io.github.danielcampossantos.profile;

import io.github.danielcampossantos.commons.ProfileUtils;
import io.github.danielcampossantos.domain.Profile;
import io.github.danielcampossantos.exception.BadRequestException;
import io.github.danielcampossantos.exception.ProfileAlreadyExistsException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileServiceTest {

    @InjectMocks
    private ProfileService service;

    @Mock
    private ProfileRepository repository;

    @InjectMocks
    private ProfileUtils profileUtils;

    private static List<Profile> profileList;

    @BeforeEach
    void init() {
        profileList = profileUtils.createNewProfileList();
    }

    @Test
    @Order(1)
    @DisplayName("findAll returns list with all profiles when argument is null")
    void findAll_ReturnsListWithAllProfiles_WhenArgumentIsNull() {
        Mockito.when(repository.findAll()).thenReturn(profileList);

        var profiles = service.findAll();

        Assertions.assertThat(profiles).isNotNull().hasSameElementsAs(profileList);
    }

    @Test
    @Order(2)
    @DisplayName("findByName returns profile found by name when successful")
    void findByName_ReturnsProfileFoundByName_WhenSuccessful() {
        var profileToFind = profileList.getFirst();
        var name = profileToFind.getName();

        Mockito.when(repository.findByNameIgnoreCase(name)).thenReturn(Optional.of(profileToFind));

        var profiles = service.findByNameOrThrowBadRequestException(name);

        Assertions.assertThat(profiles)
                .isNotNull()
                .isEqualTo(profileToFind);
    }

    @Test
    @Order(3)
    @DisplayName("findByName throws BadRequestException when profile not found")
    void findByName_ThrowsBadRequestException_WhenProfileNotFound() {
        Mockito.when(repository.findByNameIgnoreCase(ArgumentMatchers.anyString())).thenReturn(Optional.empty());


        Assertions.assertThatException()
                .isThrownBy(() -> service.findByNameOrThrowBadRequestException(ArgumentMatchers.anyString()))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @Order(4)
    @DisplayName("findAllPaginated returns paginated profile list when successful")
    void findAllPaginated_ReturnsPaginatedProfileList_WhenSuccessful() {
        var pageRequest = PageRequest.of(0, profileList.size());
        var profilePage = new PageImpl<>(profileList, pageRequest, 1);

        Mockito.when(repository.findAll(BDDMockito.any(Pageable.class))).thenReturn(profilePage);

        var profiles = service.findAllPaginated(pageRequest);

        Assertions.assertThat(profiles)
                .isNotNull()
                .isEqualTo(profiles);
    }


    @Test
    @Order(5)
    @DisplayName("findById returns profile found by id when when successful")
    void findById_ReturnsProfileFoundById_WhenSuccessful() {
        var profileToFind = profileList.getFirst();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(profileToFind));

        var profiles = service.findByIdOrThrowBadRequestException(profileToFind.getId());

        Assertions.assertThat(profiles)
                .isNotNull()
                .isEqualTo(profileToFind);

    }

    @Test
    @Order(6)
    @DisplayName("findById throws BadRequestException when profile not found")
    void findById_ThrowsBadRequestException_WhenProfileNotFound() {
        var id = 99L;

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowBadRequestException(id))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @Order(7)
    @DisplayName("save returns saved profile when successful")
    void save_ReturnsSavedProfile_WhenSuccessful() {
        var profileToSave = profileUtils.newProfileToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(profileToSave);

        var savedProfile = service.save(profileToSave);

        Assertions.assertThat(savedProfile).isNotNull().isEqualTo(profileToSave);
    }

    @Test
    @Order(8)
    @DisplayName("save throws ProfileAlreadyExistsException when profile already exists")
    void save_ThrowsProfileAlreadyExistsException_WhenProfileAlreadyExists() {
        var savedProfile = profileList.getFirst();
        var profileToSave = profileUtils.newProfileToSave().withName(savedProfile.getName());

        BDDMockito.when(repository.findByNameIgnoreCase(profileToSave.getName())).thenReturn(Optional.of(savedProfile));


        Assertions.assertThatException().isThrownBy(() -> service.save(profileToSave))
                .isInstanceOf(ProfileAlreadyExistsException.class);

    }


}