package ru.yandex.practicum.filmorate.storage.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;

@WebMvcTest(FilmController.class)
class ReviewLikeDbStorageTest {

    @MockBean
    private ReviewLikeDbStorage reviewLikeDbStorage;

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    void getNullUsefulness() {
//        String response = mockMvc.perform(get("/films"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        verify(filmService).findAll();
//        assertEquals(objectMapper.writeValueAsString(filmCollection), response);
//    }
}