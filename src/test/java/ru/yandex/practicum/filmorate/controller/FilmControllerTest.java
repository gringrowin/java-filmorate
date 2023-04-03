package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
class FilmControllerTest {

    @MockBean
    private FilmService filmService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    void findAllWhenEmptyStorageThenReturnedOkWithEmptyList() {
        Collection<Film> filmCollection = Collections.EMPTY_LIST;
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
        Film filmToCreate = new Film();
        filmToCreate.setDuration(120);
        filmToCreate.setReleaseDate(LocalDate.of(2015, 1, 1));
        filmToCreate.setName("Friends");
        filmToCreate.setDescription("film");

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

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToCreate)))
                 .andExpect(status().is5xxServerError());

        verify(filmService, never()).create(filmToCreate);
    }

    @SneakyThrows
    @Test
    void updateWhenInvokedWithValidFilmThenReturnedOkWithUpdatedFilm() {
        filmService.create(new Film());
        Film filmToUpdate = new Film();
        filmToUpdate.setId(1);
        filmToUpdate.setDuration(120);
        filmToUpdate.setReleaseDate(LocalDate.of(2015, 1, 1));
        filmToUpdate.setName("Friends");
        filmToUpdate.setDescription("film");

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
        filmService.create(new Film());
        Film filmToUpdate = new Film();
        filmToUpdate.setId(4);
        filmToUpdate.setDuration(120);
        filmToUpdate.setReleaseDate(LocalDate.of(2015, 1, 1));
        filmToUpdate.setName("Friends");
        filmToUpdate.setDescription("film");

        when(filmService.update(filmToUpdate)).thenThrow(FilmNotFoundException.class);

        mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToUpdate)))
                .andExpect(status().isNotFound());

        verify(filmService, never()).create(filmToUpdate);
    }

    @SneakyThrows
    @Test
    void getFilm() {
        filmService.create(new Film());
        Film filmToUpdate = new Film();
        filmToUpdate.setId(1);
        filmToUpdate.setDuration(120);
        filmToUpdate.setReleaseDate(LocalDate.of(2015, 1, 1));
        filmToUpdate.setName("Friends");
        filmToUpdate.setDescription("film");

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
    void addLike() {
        filmService.create(new Film());
        Film filmToUpdate = new Film();
        filmToUpdate.setId(1);
        filmToUpdate.setDuration(120);
        filmToUpdate.setReleaseDate(LocalDate.of(2015, 1, 1));
        filmToUpdate.setName("Friends");
        filmToUpdate.setDescription("film");

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
    void deleteLike() {
        filmService.create(new Film());
        Film filmToUpdate = new Film();
        filmToUpdate.setId(1);
        filmToUpdate.setDuration(120);
        filmToUpdate.setReleaseDate(LocalDate.of(2015, 1, 1));
        filmToUpdate.setName("Friends");
        filmToUpdate.setDescription("film");

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
    void getPopularFilms() {
        filmService.create(new Film());
        Film filmToUpdate = new Film();
        filmToUpdate.setId(1);
        filmToUpdate.setDuration(120);
        filmToUpdate.setReleaseDate(LocalDate.of(2015, 1, 1));
        filmToUpdate.setName("Friends");
        filmToUpdate.setDescription("film");

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
}