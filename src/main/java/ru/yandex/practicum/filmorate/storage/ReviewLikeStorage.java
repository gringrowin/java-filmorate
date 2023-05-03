package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

public interface ReviewLikeStorage {
    Review addLike(int reviewId, int userId);

    Review deleteLike(int reviewId, int userId);

    Review addDisLike(int reviewId, int userId);

    Review deleteDisLike(int reviewId, int userId);

    Integer getUsefulness(int id);
}
