package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dao.ReviewDbStorage;

public class ReviewService {
    ReviewDbStorage reviewDbStorage;

    @Autowired
    public ReviewService(ReviewDbStorage reviewDbStorage) {
        this.reviewDbStorage = reviewDbStorage;
    }

    public Review addNewReview(Review review) {
        return reviewDbStorage.addNewReview(review);
    }
}
