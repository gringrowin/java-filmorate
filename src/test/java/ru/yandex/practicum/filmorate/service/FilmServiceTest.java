package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @InjectMocks
    private FilmService filmService;

    @Mock
    private FilmStorage filmStorage;
    @Mock
    private GenreStorage genreStorage;
    @Mock
    private MpaStorage mpaStorage;
    @Mock
    private LikeStorage likeStorage;

    private final Film testFilm = new Film();

    @BeforeEach
    void initializationTestFilm() {
        testFilm.setId(1);
        testFilm.setDuration(120);
        testFilm.setReleaseDate(LocalDate.of(2015, 1, 1));
        testFilm.setName("Friends");
        testFilm.setDescription("film");
        Mpa mpa = new Mpa();
        mpa.setId(1);
        testFilm.setMpa(mpa);
    }

    @Test
    void findAllWhenStorageNotEmptyThenReturnedList() {
        List<Film> filmsExcept = List.of(testFilm);
        when(filmStorage.getAll()).thenReturn(filmsExcept);
        when(genreStorage.getGenresByFilmFromStorage(testFilm.getId()))
                .thenReturn(Collections.emptySet());
        when(likeStorage.getLikes(testFilm.getId())).thenReturn(0);
        when(mpaStorage.getMpa(testFilm.getMpa().getId())).thenReturn(testFilm.getMpa());

        List<Film> films = filmService.findAll();

        verify(filmStorage).getAll();
        assertEquals(filmsExcept, films);
    }
}