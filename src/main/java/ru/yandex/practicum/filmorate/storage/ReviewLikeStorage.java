package ru.yandex.practicum.filmorate.storage;

public interface ReviewLikeStorage {
    void addReaction(int reviewId, int userId, boolean isLike);

    void deleteReaction(int reviewId, int userId);

    Integer getUsefulness(int id);
}
