package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewStorage {
    Review addNewReview(Review review);

    Review update(Review review);

    void delete(int id);

    Optional<Review> getReviewById(int id);

    Collection<Review> getReviews();
}
