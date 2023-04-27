package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Set;

public interface GenreStorage {

    public Collection<Genre> getAll();

    Genre getGenre(Integer genreId);

    Film updateGenreByFilmToStorage(Film film);

    Set<Genre> getGenresByFilmFromStorage(Integer filmId);
}
