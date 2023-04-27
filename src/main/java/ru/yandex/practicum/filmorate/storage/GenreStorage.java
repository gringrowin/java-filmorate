package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Set;

public interface GenreStorage {

    Collection<Genre> getAll();

    Genre getGenre(Integer genreId);

    void updateGenreByFilmToStorage(Film film);

    Set<Genre> getGenresByFilmFromStorage(Integer filmId);
}
