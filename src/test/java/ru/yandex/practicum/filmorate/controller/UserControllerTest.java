package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
//
//    @Test
//    void findAll() {
//    }
//
@SneakyThrows
@Test
void create() {
    User userToCreate = new User();
    userToCreate.setLogin("login");
    userToCreate.setEmail("mail@mail.ru");
    userToCreate.setBirthday(LocalDate.of(2000, 12, 4));
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
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void getUser() {
//    }
//
//    @Test
//    void addFriend() {
//    }
//
//    @Test
//    void deleteFriend() {
//    }
//
//    @Test
//    void getFriends() {
//    }
//
//    @Test
//    void getCommonFriends() {
//    }
}