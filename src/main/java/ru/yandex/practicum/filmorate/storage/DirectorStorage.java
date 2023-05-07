package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;

public interface DirectorStorage {
    Director create(Director director);

    Director update(Director director);

    Director getById(Integer directorId);

    Set<Director> getByFilmId(Integer filmId);

    void updateDirectorsByFilmToStorage(Film film);

    Set<Director> getDirectorsByFilmFromStorage(Integer filmId);

    Set<Director> getAll();

    Director delete(Integer directorId);
}
