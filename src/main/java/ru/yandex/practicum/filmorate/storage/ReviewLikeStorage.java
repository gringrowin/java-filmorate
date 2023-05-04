package ru.yandex.practicum.filmorate.storage;

public interface ReviewLikeStorage {
    void addLike(int reviewId, int userId, boolean isLike);

    void deleteLike(int reviewId, int userId);

   Integer getUsefulness(int id);
}
