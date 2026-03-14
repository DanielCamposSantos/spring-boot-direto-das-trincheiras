package io.github.danielcampossantos.user;

import io.github.danielcampossantos.commons.FileUtils;
import io.github.danielcampossantos.commons.UserUtils;
import io.github.danielcampossantos.commons.ValidationErrors;
import io.github.danielcampossantos.domain.User;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@WebMvcTest(UserController.class)
@ComponentScan(basePackages = {"io.github.danielcampossantos.user", "io.github.danielcampossantos.commons"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    private static final String URL = "/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private UserUtils userUtils;

    @MockitoBean
    private UserRepository repository;


    private static List<User> userList;


    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }


    @Test
    @DisplayName("GET /v1/users returns list with all users when successful")
    @SneakyThrows
    @Order(1)
    void findAll_ReturnsListWithAllUsers_WhenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var response = fileUtils.readResourceFile("user/get-user-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET /v1/users?name=Cezar returns list user found by name when successful")
    @SneakyThrows
    @Order(2)
    void findAll_ReturnsListWithUserFoundByName_WhenNameIsNull() {
        var response = fileUtils.readResourceFile("user/get-user-cezar-name-200.json");
        var firstName = "Cezar";

        var usersByFirstName = userList.stream().filter(user -> user.getFirstName().equals(firstName)).toList();

        BDDMockito.when(repository.findByFirstNameIgnoreCase(firstName)).thenReturn(usersByFirstName);


        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", firstName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET /v1/users?name=x returns empty list when user not found")
    @SneakyThrows
    @Order(3)
    void findAll_ReturnsEmptyList_WhenUserNotFound() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var response = fileUtils.readResourceFile("user/get-user-x-name-200.json");

        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET /v1/users/1 returns user by id when successful")
    @SneakyThrows
    @Order(4)
    void findById_ReturnsUserById_WhenSuccessful() {
        var response = fileUtils.readResourceFile("user/get-user-by-id-200.json");

        var id = 1L;
        var userOptional = userList.stream().filter(user -> user.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(userOptional);


        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET /v1/users/99 throws BadRequestException")
    @SneakyThrows
    @Order(5)
    void findById_ThrowsBadRequestException_WhenUserNotFoundById() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var response = fileUtils.readResourceFile("user/get-user-by-id-400.json");

        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST /v1/users creates user when successful")
    @SneakyThrows
    @Order(6)
    void save_CreatesUser_WhenSuccessful() {
        var request = fileUtils.readResourceFile("user/post-request-user-200.json");
        var response = fileUtils.readResourceFile("user/post-response-user-201.json");

        var userToSave = userUtils.newUserToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(userToSave);


        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE /v1/users/1 removes user when successful")
    @SneakyThrows
    @Order(7)
    void delete_RemovesUser_WhenSuccessful() {
        var id = 1L;
        var userOptional = userList.stream().filter(user -> user.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(userOptional);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    @DisplayName("DELETE /v1/users/99 throws BadRequestException")
    @SneakyThrows
    @Order(8)
    void delete_ThrowsBadRequestException_WhenUserNotFound() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var id = 99L;
        var response = fileUtils.readResourceFile("user/delete-user-by-id-400.json");


        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("PUT /v1/users updates user when successful")
    @SneakyThrows
    @Order(9)
    void update_UpdatesUser_WhenSuccessful() {
        var id = 1L;
        var userToUpdate = userList.getFirst().withId(id);

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));

        var request = fileUtils.readResourceFile("user/put-request-user-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @DisplayName("PUT /v1/users throws BadRequestException")
    @SneakyThrows
    @Order(10)
    void update_ThrowsBadRequestException_WhenUserNotFound() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var request = fileUtils.readResourceFile("user/put-request-user-404.json");

        var response = fileUtils.readResourceFile("user/put-user-by-id-400.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST /v1/users returns bad request when fields are invalid")
    @SneakyThrows
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) {
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

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

    private static Stream<Arguments> postUserBadRequestSource() {
        var allRequiredErrors = ValidationErrors.allRequiredErrors();
        var emailInvalidError = ValidationErrors.invalidEmailErrors();

        return Stream.of(
                Arguments.of("post-request-user-empty-field-400.json", allRequiredErrors),
                Arguments.of("post-request-user-blank-field-400.json", allRequiredErrors),
                Arguments.of("post-request-user-null-field-400.json", allRequiredErrors),
                Arguments.of("post-request-user-invalid-email-400.json", emailInvalidError)
        );
    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT /v1/users throws BadRequestException")
    @SneakyThrows
    @Order(10)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);

    }

    private static Stream<Arguments> putUserBadRequestSource() {
        var allRequiredErrors = ValidationErrors.allRequiredErrors();
        allRequiredErrors.add("The field 'id' is can not be null");
        var emailInvalidError = ValidationErrors.invalidEmailErrors();
        return Stream.of(
                Arguments.of("put-request-user-empty-field-400.json", allRequiredErrors),
                Arguments.of("put-request-user-blank-field-400.json", allRequiredErrors),
                Arguments.of("put-request-user-null-field-400.json", allRequiredErrors),
                Arguments.of("put-request-user-invalid-email-400.json", emailInvalidError)
        );
    }


}