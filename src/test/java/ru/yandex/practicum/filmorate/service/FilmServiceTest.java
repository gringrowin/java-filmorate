package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @InjectMocks
    private FilmService filmService;

    @Mock
    private FilmStorage filmStorage;

    private final Film testFilm = new Film();

    @BeforeEach
    void initializationTestFilm() {
        testFilm.setId(1);
        testFilm.setDuration(120);
        testFilm.setReleaseDate(LocalDate.of(2015, 1, 1));
        testFilm.setName("Friends");
        testFilm.setDescription("film");
    }

    @Test
    void findAllWhenStorageNotEmptyThenReturnedList() {
        Collection<Film> filmsExcept = List.of(testFilm);
        when(filmStorage.getAll()).thenReturn(filmsExcept);

        Collection<Film> films = filmService.findAll();

        verify(filmStorage).getAll();
        assertEquals(filmsExcept, films);
    }

    @Test
    void createFilmThenReturnedFilm() {
        Film filmExcept = testFilm;
        when(filmStorage.add(filmExcept)).thenReturn(filmExcept);

        Film film = filmService.create(testFilm);

        verify(filmStorage).add(film);
        assertEquals(filmExcept, film);
    }

    @Test
    void updateFilmThenReturnedUpdatedFilm() {
        Film filmExcept = testFilm;
        when(filmStorage.update(filmExcept)).thenReturn(filmExcept);

        Film film = filmService.update(testFilm);

        verify(filmStorage).update(testFilm);
        assertEquals(filmExcept, film);
    }

    @Test
    void getFilmWhenIdNotFoundThenThrowFilmNotFoundException() {
        int id = testFilm.getId();
        when(filmStorage.getFilm(id)).thenReturn(null);
        assertThrows(FilmNotFoundException.class, () -> filmService.getFilm(id));
    }

    @Test
    void getFilmWhenCorrectIdThenReturnedFilm() {
        Film filmExcept = testFilm;
        int id = filmExcept.getId();
        when(filmStorage.getFilm(id)).thenReturn(filmExcept);

        Film film = filmService.getFilm(id);

        verify(filmStorage).getFilm(id);
        assertEquals(filmExcept, film);
    }



}