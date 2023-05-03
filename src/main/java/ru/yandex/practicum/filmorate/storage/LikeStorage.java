package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeStorage {
    Film addLike(Film film, Integer userId);

    Film deleteLike(Film film, Integer userId);

    Integer getLikes(Integer filmId);

    List<Integer> getLikedFilmsByUserId(int userId);
}
