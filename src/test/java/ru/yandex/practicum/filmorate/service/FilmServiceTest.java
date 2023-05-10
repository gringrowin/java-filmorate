package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    private final Film testFilm = new Film();

    @InjectMocks
    private FilmService filmService;

    @Mock
    private GenreService genreService;
    @Mock
    private MpaService mpaService;
    @Mock
    private LikeService likeService;
    @Mock
    private DirectorService directorService;
    @Mock
    private FilmStorage filmStorage;


    @BeforeEach
    void initializationTestFilm() {
        testFilm.setId(1);
        testFilm.setName("RoboCop");
        testFilm.setGenres(Set.of(new Genre("Genre", 1)));
        testFilm.setLikes(1);
        testFilm.setMpa(new Mpa().withId(1));
        testFilm.setDirectors(Set.of(new Director()));
    }

    @Test
    void findAllWhenTestFilmInStorageThenReturnedList() {
        List<Film> testfilms = List.of(testFilm);
        when(filmStorage.getAll()).thenReturn(testfilms);

        List<Film> films = filmService.findAll();

        verify(filmStorage).getAll();

        assertEquals(testfilms, films);
    }

    @Test
    void updateFilmWhenStorageNotEmptyThenReturnedUpdatedFilm() {
        Film updatedFilm = testFilm;
        Film newFilm = testFilm.withId(1);

        when(filmStorage.getFilm(newFilm.getId())).thenReturn(newFilm);
        when(filmStorage.update(updatedFilm)).thenReturn(newFilm);

        Film film = filmService.update(updatedFilm);

        verify(filmStorage).update(updatedFilm);
        assertEquals(newFilm, film);
    }
}