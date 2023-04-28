package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface LikeStorage {
    Film addLike(Film film, Integer userId);

    Film deleteLike(Film film, Integer userId);

    Integer getLikes(Integer filmId);
}
