package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private FeedService feedService;
    @MockBean
    private FriendService friendService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private final User testUser = new User();

    @BeforeEach
    void initializationTestUser() {
        testUser.setId(1);
        testUser.setLogin("login");
        testUser.setName("name");
        testUser.setBirthday(LocalDate.of(2000, 1,1));
        testUser.setEmail("mail@mail.ru");
    }

    @SneakyThrows
    @Test
    void findAllWhenEmptyStorageThenReturnedOkWithEmptyList() {
        List<User> allUsers = Collections.emptyList();
        when(userService.findAll()).thenReturn(allUsers);

        String response = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService).findAll();
        assertEquals(objectMapper.writeValueAsString(allUsers), response);
    }

    @SneakyThrows
    @Test
    void createWhenInvokedWithValidUserThenReturnedOkWithCreatedUser() {
        User userToCreate = testUser;

        when(userService.create(userToCreate)).thenReturn(userToCreate);

        String response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService).create(userToCreate);
        assertEquals(objectMapper.writeValueAsString(userToCreate), response);
    }

    @SneakyThrows
    @Test
    void createWhenInvokedWithInvalidUserThenReturned5xxServerError() {
        User userToCreate = new User();

        when(userService.create(userToCreate)).thenThrow(ValidationException.class);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().is5xxServerError());

        verify(userService, never()).create(userToCreate);
    }

    @SneakyThrows
    @Test
    void updateWhenInvokedWithValidUserThenReturnedOkWithUpdatedUser() {
        User userToUpdate = testUser;

        when(userService.update(userToUpdate)).thenReturn(userToUpdate);

        String response = mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService).update(userToUpdate);
        assertEquals(objectMapper.writeValueAsString(userToUpdate), response);
    }

    @SneakyThrows
    @Test
    void updateWhenInvokedWithInvalidIdUserThenReturned404NotFound() {
        User userToUpdate = testUser;
        userToUpdate.setId(4);

        when(userService.update(userToUpdate)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isNotFound());

        verify(userService, never()).create(userToUpdate);
    }

    @SneakyThrows
    @Test
    void getUserWhenInvokedWithInvalidIdUserThenReturned404NotFound() {

        int id = 1;
        when(userService.getUser(id)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isNotFound());

        verify(userService).getUser(id);
    }

    @SneakyThrows
    @Test
    void getUserWhenInvokedWithValidIdUserThenReturnedWithUser() {
        User userToGet = testUser;
        int id = 1;
        when(userService.getUser(id)).thenReturn(userToGet);

        String response = mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService).getUser(id);
        assertEquals(objectMapper.writeValueAsString(userToGet), response);
    }
}