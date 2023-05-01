package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    Film add(Film film);

    Film update(Film film);

    Film getFilm(Integer id);

    List<Film> getPopularFilmsGenreAndYearFiltered(Integer genreId, Integer year, Integer limit);
}
