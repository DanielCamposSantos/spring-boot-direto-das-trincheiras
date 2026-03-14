package io.github.danielcampossantos.profile;

import io.github.danielcampossantos.commons.FileUtils;
import io.github.danielcampossantos.commons.ProfileUtils;
import io.github.danielcampossantos.domain.Profile;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@WebMvcTest(controllers = ProfileController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"io.github.danielcampossantos.profile", "io.github.danielcampossantos.commons"})
class ProfileControllerTest {
    private static final String URL = "/profiles";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private ProfileUtils profileUtils;

    @MockitoBean
    private ProfileRepository repository;

    private static List<Profile> profileList;

    @BeforeEach
    void init() {
        profileList = profileUtils.createNewProfileList();
    }


    @Test
    @Order(1)
    @DisplayName("GET /profiles returns list with all profiles when argument is null")
    @SneakyThrows
    void findAll_ReturnsListWithAllProfiles_WhenArgumentIsNull() {
        var response = fileUtils.readResourceFile("profile/get-profile-null-name-200.json");

        BDDMockito.when(repository.findAll()).thenReturn(profileList);

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @Order(2)
    @DisplayName("GET /profiles?name=Silviodino returns list with profile found by name when successful")
    @SneakyThrows
    void findAll_ReturnsListWithProfileFoundByName_WhenSuccessful() {
        var response = fileUtils.readResourceFile("profile/get-profile-silviodino-name-200.json");

        var profileToFind = profileList.getFirst();
        var name = profileToFind.getName();

        BDDMockito.when(repository.findByNameIgnoreCase(name)).thenReturn(Collections.singletonList(profileToFind));


        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(3)
    @DisplayName("GET /profiles?name=x returns empty list when profile not found")
    @SneakyThrows
    void findAll_ReturnsListWithProfileFoundByName_WhenProfileNotFound() {
        var response = fileUtils.readResourceFile("profile/get-profile-x-name-200.json");
        var name = "x";

        BDDMockito.when(repository.findByNameIgnoreCase(name)).thenReturn(Collections.emptyList());


        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(4)
    @DisplayName("findAllPaginated returns paginated profile list when successful")
    @SneakyThrows
    void findAllPaginated_ReturnsPaginatedProfileList_WhenSuccessful() {
        var response = fileUtils.readResourceFile("profile/get-profile-paginated-200.json");

        var pageRequest = PageRequest.of(0, profileList.size());
        var profilePage = new PageImpl<>(profileList, pageRequest, 1);

        Mockito.when(repository.findAll(BDDMockito.any(Pageable.class))).thenReturn(profilePage);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/paginated"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(5)
    @DisplayName("GET /profiles/1 returns profile found by id when when successful")
    @SneakyThrows
    void findById_ReturnsProfileFoundById_WhenSuccessful() {
        var response = fileUtils.readResourceFile("profile/get-profile-by-id-200.json");

        var profileToFind = profileList.getFirst();
        var id = profileToFind.getId();
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(profileToFind));

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(6)
    @DisplayName("GET /profiles/99 throws BadRequestException when profile not found")
    @SneakyThrows
    void findById_ThrowsBadRequestException_WhenProfileNotFound() {
        var response = fileUtils.readResourceFile("profile/get-profile-by-id-400.json");

        var id = 99L;
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(7)
    @DisplayName("POST /profiles returns saved profile when successful")
    @SneakyThrows
    void save_ReturnsSavedProfile_WhenSuccessful() {
        var request = fileUtils.readResourceFile("profile/post-request-profile-200.json");
        var response = fileUtils.readResourceFile("profile/post-response-profile-201.json");

        var profileToSave = profileUtils.newUserToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(profileToSave);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("getProfilesBadRequestSources")
    @Order(8)
    @DisplayName("POST /profiles throws BadRequestException when invalid fields")
    @SneakyThrows
    void save_ThrowsBadRequestException_WhenInvalidFields(String fileName, List<String> errors) {
        var request = fileUtils.readResourceFile("profile/%s".formatted(fileName));

        var profileToSave = profileUtils.newUserToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(profileToSave);

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage())
                .contains(errors);
    }

    private static Stream<Arguments> getProfilesBadRequestSources() {
        var nameErrorMessage = "The field 'name' is required";
        var descriptionErrorMessage = "The field 'description' is required";
        var errors = new ArrayList<>(List.of(nameErrorMessage, descriptionErrorMessage));

        return Stream.of(
                Arguments.of("post-request-profile-blank-field-400.json", errors),
                Arguments.of("post-request-profile-empty-field-400.json", errors),
                Arguments.of("post-request-profile-null-field-400.json", errors)
        );
    }

}