package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DirectorServiceTest {
    @InjectMocks
    private DirectorService directorService;
    @Mock
    private DirectorStorage directorStorage;

    @DisplayName("Проверка команды сервиса по созданию режиссёра")
    @Test
    void shouldCreateDirector() {
        Director director = createTestDirector(1);

        when(directorStorage.create(director)).thenReturn(director);
        assertThat(directorService.create(director)).isEqualTo(director);
        verify(directorStorage).create(director);
    }

    @DisplayName("Проверка команды сервиса по обновлению режиссёра")
    @Test
    void shouldUpdateDirector() {
        Director director = createTestDirector(1);
        when(directorStorage.get(director.getId())).thenReturn(director);
        when(directorStorage.update(director)).thenReturn(director);

        assertThat(directorService.update(director)).isEqualTo(director);
        verify(directorStorage).update(director);
    }

    @DisplayName("Проверка команды сервиса по получению режиссёра по id")
    @Test
    void shouldGetDirector() {
        int id = 1;
        Director director = createTestDirector(id);
        when(directorStorage.get(id)).thenReturn(director);

        assertThat(directorService.get(id)).isEqualTo(director);
        verify(directorStorage).get(id);
    }

    @DisplayName("Проверка команды сервиса по получению режиссёра по несуществующему id")
    @Test
    void shouldGetDirectorNotFoundExceptionBecauseOfNonexistentId() {
        int id = 1000;
        when(directorStorage.get(id)).thenThrow(new DirectorNotFoundException(
                String.format("Director with id %s is not found!", id)));

        DirectorNotFoundException exception = assertThrows(DirectorNotFoundException.class,
                () -> directorService.get(id));
        assertThat(exception.getMessage()).isEqualTo("Director with id %s is not found!", id);
    }

    @DisplayName("Проверка команды сервиса по получению всех режиссёров при пустом списке")
    @Test
    void shouldReturnEmptySetIfItIsEmpty() {
        when(directorStorage.getAll()).thenReturn(Collections.emptySet());
        assertThat(directorService.getAll()).isEqualTo(Collections.emptySet());
    }

    @DisplayName("Проверка команды сервиса по получению всех режиссёров")
    @Test
    void shouldReturnSetOfDirectorsIfItIsNotEmpty() {
        Set<Director> directors = Set.of(createTestDirector(1),
                createTestDirector(2).withName("Klim Shipenko"),
                createTestDirector(3).withName("Stanley Kubrick"));

        when(directorStorage.getAll()).thenReturn(directors);
        assertThat(directorService.getAll()).isEqualTo(directors);
    }

    @DisplayName("Проверка команды сервиса по удалению режиссёра по несуществующему id")
    @Test
    void shouldThrowDirectorNotFoundExceptionBecauseOfNonexistentId() {
        int id = 1000;

        DirectorNotFoundException exception = assertThrows(DirectorNotFoundException.class,
                () -> directorService.delete(id));
        assertThat(exception.getMessage()).isEqualTo("Director with id %s is not found!", id);
    }

    @DisplayName("Проверка команды сервиса по удалению режиссёра по id")
    @Test
    void shouldDeleteDirector() {
        int id = 1;
        Director director = createTestDirector(id);
        when(directorStorage.delete(id)).thenReturn(true);
        assertThat(directorStorage.delete(id)).isEqualTo(true);
    }

    private Director createTestDirector(int id) {
        return new Director(
                id,
                "Quentin Tarantino"
        );
    }
}
