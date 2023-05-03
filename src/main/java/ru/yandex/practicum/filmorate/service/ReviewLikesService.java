package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dao.ReviewDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.ReviewLikeDbStorage;

@Service
public class ReviewLikesService {
    ReviewLikeDbStorage reviewLikeDbStorage;
    ReviewDbStorage reviewDbStorage;

    @Autowired
    public ReviewLikesService(ReviewLikeDbStorage reviewLikeDbStorage, ReviewDbStorage reviewDbStorage) {
        this.reviewLikeDbStorage = reviewLikeDbStorage;
        this.reviewDbStorage = reviewDbStorage;
    }

    public Review addLikeToReview(int reviewId, int userId) {
        reviewLikeDbStorage.addLike(reviewId, userId);
        return reviewDbStorage.getReviewById(reviewId).orElseThrow();
    }

    public Review addDisLikeToReview(int reviewId, int userId) {
        return reviewLikeDbStorage.addDisLike(reviewId, userId);
    }

    public Review deleteLikeFromReview(int reviewId, int userId) {
        return reviewLikeDbStorage.deleteLike(reviewId, userId);
    }

    public Review deleteDisLikeFromReview(int reviewId, int userId) {
        return reviewLikeDbStorage.deleteDisLike(reviewId, userId);
    }
}
