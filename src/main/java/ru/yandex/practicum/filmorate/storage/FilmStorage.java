package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Collection<Film> getAll();

    Film add(Film film);

    Film update(Film film);

    Film getFilm(Integer id);

    Film addLike(Integer filmId, Integer userId);

    Film deleteLike(Integer filmId, Integer userId);

    List<Film> getPopularFilms(Integer count);
}
