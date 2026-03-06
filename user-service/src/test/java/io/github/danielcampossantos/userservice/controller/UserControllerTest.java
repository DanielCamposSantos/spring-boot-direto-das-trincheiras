package io.github.danielcampossantos.userservice.controller;

import io.github.danielcampossantos.userservice.commons.FileUtils;
import io.github.danielcampossantos.userservice.commons.UserUtils;
import io.github.danielcampossantos.userservice.domain.User;
import io.github.danielcampossantos.userservice.repository.UserData;
import io.github.danielcampossantos.userservice.repository.UserHardCodedRepository;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@WebMvcTest(UserController.class)
@ComponentScan("io.github.danielcampossantos")
class UserControllerTest {
    private static final String URL = "/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private UserUtils userUtils;
    
    
    @MockitoBean
    private UserData userData;
    
    @MockitoSpyBean
    private UserHardCodedRepository repository;


    private static List<User> userList;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }


    @Test
    @DisplayName("GET /v1/users returns list with all users when successful")
    @SneakyThrows
    void findAll_ReturnsListWithAllUsers_WhenNameIsNull() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var response = fileUtils.readResourceFile("user/get-user-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET /v1/users?name=Cezar returns list user found by name when successful")
    @SneakyThrows
    void findAll_ReturnsListWithUserFoundByName_WhenNameIsNull() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var response = fileUtils.readResourceFile("user/get-user-cezar-name-200.json");

        var name = "Cezar";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name",name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET /v1/users?name=x returns empty list when user not found")
    @SneakyThrows
    void findAll_ReturnsEmptyList_WhenUserNotFound() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var response = fileUtils.readResourceFile("user/get-user-x-name-200.json");

        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name",name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET /v1/users/1 returns user by id when successful")
    @SneakyThrows
    void findById_ReturnsUserById_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var response = fileUtils.readResourceFile("user/get-user-by-id-200.json");

        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET /v1/users/99 throws ResponseStatusException")
    @SneakyThrows
    void findById_ThrowsResponseStatusException_WhenUserNotFoundById() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
//
//    @Test
//    @DisplayName("POST /v1/users creates user when successful")
//    @SneakyThrows
//    void save_CreatesUser_WhenSuccessful() {
//        var userToSaved = userUtils.newUserToSave();
//
//        BDDMockito.when(repository.save(userToSaved)).thenReturn(userToSaved);
//
//        var request = fileUtils.readResourceFile("user/post-user-request-200.json");
//
//        var response = fileUtils.readResourceFile("user/post-user-response-201.json");
//
//        mockMvc.perform(MockMvcRequestBuilders.get(URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(request)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.content().json(response));
//    }


}