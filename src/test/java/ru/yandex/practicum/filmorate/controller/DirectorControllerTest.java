package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
public class DirectorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DirectorService directorService;

    @DisplayName("Проверка метода контроллера по созданию режиссёра")
    @Test
    void shouldCreateDirector() throws Exception {
        Director director = createTestDirector(1);
        String json = objectMapper.writeValueAsString(director);

        when(directorService.create(director)).thenReturn(director);

        mockMvc.perform(post("/directors").contentType("application/json").content(json))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(director)));
    }

    @DisplayName("Проверка метода контроллера по обновлению режиссёра")
    @Test
    void shouldUpdateDirector() throws Exception {
        Director director = createTestDirector(1).withName("Klim Shipenko");
        String json = objectMapper.writeValueAsString(director);

        when(directorService.update(director)).thenReturn(director);

        mockMvc.perform(put("/directors").contentType("application/json").content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(director)));
    }

    @DisplayName("Проверка методов создания/обновления режиссёра с некорректными данными")
    @Test
    void shouldNotCreateOrUpdateDirector() throws Exception {
        Director invalidDirector = createTestDirector(1).withId(-10).withName("           ");
        String json = objectMapper.writeValueAsString(invalidDirector);

        mockMvc.perform(post("/directors").contentType("application/json").content(json))
                .andExpect(status().isInternalServerError());
        mockMvc.perform(put("/directors").contentType("application/json").content(json))
                .andExpect(status().isInternalServerError());
    }

    @DisplayName("Проверка метода контроллера по получению режиссёра по id")
    @Test
    void shouldGetDirector() throws Exception {
        int id = 1;
        Director director = createTestDirector(id);

        when(directorService.get(id)).thenReturn(director);

        mockMvc.perform(get("/directors/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(director)));
    }

    @DisplayName("Проверка метода контроллера по получению режиссёра по несуществующему id")
    @Test
    void shouldGetDirectorNotFoundExceptionBecauseOfNonexistentId() throws Exception {
        Director director = createTestDirector(1000);

        String error = String.format("Director with id %s is not found!", director.getId());

        when(directorService.get(director.getId())).thenThrow(new DirectorNotFoundException(
                String.format("Director with id %s is not found!", director.getId())));
        
        mockMvc.perform(get("/directors/" + director.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(new ErrorResponse(error))));
    }

    @DisplayName("Проверка метода контроллера по получению всех режиссёров, когда список пуст")
    @Test
    void shouldReturnDirectorSetIfDirectorsTableIsEmpty() throws Exception {
        mockMvc.perform(get("/directors"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @DisplayName("Проверка метода контроллера по получению всех режиссёров")
    @Test
    void shouldReturnDirectorSet() throws Exception {
        Set<Director> directors = Set.of(createTestDirector(1), createTestDirector(2).withName("Klim Shipenko"));

        when(directorService.getAll()).thenReturn(directors);

        mockMvc.perform(get("/directors"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(directors)));
    }

    @DisplayName("Проверка метода контроллера по удалению фильма по id")
    @Test
    void shouldDeleteDirector() throws Exception {
        int id = 1;
        mockMvc.perform(delete("/directors/" + id))
                .andExpect(status().isOk());
    }

    private Director createTestDirector(int id) {
        return new Director(
                id,
                "Quentin Tarantino"
        );
    }
}
