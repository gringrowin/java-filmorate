package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
class FilmControllerTest {

    @MockBean
    private FilmService filmService;

    @MockBean
    private LikeService likeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private final Film testFilm = new Film();

    @BeforeEach
    void initializationTestFilm() {
        testFilm.setId(1);
        testFilm.setDuration(120);
        testFilm.setReleaseDate(LocalDate.of(2015, 1, 1));
        testFilm.setName("Friends");
        testFilm.setDescription("film");
    }

    @SneakyThrows
    @Test
    void findAllWhenEmptyStorageThenReturnedOkWithEmptyList() {
        List<Film> filmCollection = Collections.emptyList();
        when(filmService.findAll()).thenReturn(filmCollection);

        String response = mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(filmService).findAll();
        assertEquals(objectMapper.writeValueAsString(filmCollection), response);
    }

    @SneakyThrows
    @Test
    void findAllWhenTestFilmInStorageThenReturnedOkWithTestFilm() {
        List<Film> filmCollection = List.of(testFilm);
        when(filmService.findAll()).thenReturn(filmCollection);

        String response = mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(filmService).findAll();
        assertEquals(objectMapper.writeValueAsString(filmCollection), response);
    }

    @SneakyThrows
    @Test
    void createWhenInvokedWithValidFilmThenReturnedOkWithCreatedFilm() {
        Film filmToCreate = testFilm;

        when(filmService.create(filmToCreate)).thenReturn(filmToCreate);

        String response = mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(filmToCreate)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        verify(filmService).create(filmToCreate);
        assertEquals(objectMapper.writeValueAsString(filmToCreate), response);
    }

    @SneakyThrows
    @Test
    void createWhenInvokedWithInvalidFilmThenReturned5xxServerError() {
        Film filmToCreate = new Film();

        when(filmService.create(filmToCreate)).thenThrow(ValidationException.class);

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToCreate)))
                 .andExpect(status().is5xxServerError());

        verify(filmService, never()).create(filmToCreate);
    }

    @SneakyThrows
    @Test
    void updateWhenInvokedWithValidFilmThenReturnedOkWithUpdatedFilm() {
        Film filmToUpdate = testFilm;

        when(filmService.update(filmToUpdate)).thenReturn(filmToUpdate);

        String response = mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToUpdate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(filmService).update(filmToUpdate);
        assertEquals(objectMapper.writeValueAsString(filmToUpdate), response);
    }

    @SneakyThrows
    @Test
    void updateWhenInvokedWithInvalidIdFilmThenReturned404NotFound() {
        Film filmToUpdate = testFilm;
        filmToUpdate.setId(4);

        when(filmService.update(filmToUpdate)).thenThrow(FilmNotFoundException.class);

        mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToUpdate)))
                .andExpect(status().isNotFound());

        verify(filmService, never()).create(filmToUpdate);
    }

    @SneakyThrows
    @Test
    void getFilmWhenInvokedWithInvalidIdFilmThenReturned404NotFound() {
        int id = 1;
        when(filmService.getFilm(id)).thenThrow(FilmNotFoundException.class);

        mockMvc.perform(get("/films/{id}", id))
                .andExpect(status().isNotFound());

        verify(filmService).getFilm(id);
    }

    @SneakyThrows
    @Test
    void getFilmWhenInvokedWithValidIdFilmThenReturnedWithFilm() {
        Film filmToGet = testFilm;
        int id = 1;
        when(filmService.getFilm(id)).thenReturn(filmToGet);

        String response = mockMvc.perform(get("/films/{id}", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(filmService).getFilm(id);
        assertEquals(objectMapper.writeValueAsString(filmToGet), response);
    }
}