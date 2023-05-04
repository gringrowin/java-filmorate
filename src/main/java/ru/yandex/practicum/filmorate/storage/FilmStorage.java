package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.enums.FilmSortBy;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    Film add(Film film);

    Film update(Film film);

    Film getFilm(Integer id);

    List<Film> getFilmsByDirectorIdAndSort(Integer directorId, FilmSortBy sortBy);

    List<Film> getPopularFilms(Integer count, Integer genreId, Integer year);

    List<Film> getCommonFilmsForFriendSortedByPopular(Integer userId, Integer friendId);
}
