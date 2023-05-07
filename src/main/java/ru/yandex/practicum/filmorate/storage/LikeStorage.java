package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface LikeStorage {
    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    Integer getLikes(Integer filmId);

    List<Integer> getLikedFilmsByUserId(int userId);
}
