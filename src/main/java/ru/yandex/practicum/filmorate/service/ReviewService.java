package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dao.ReviewDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.ReviewLikeDbStorage;

import java.util.Collection;

@Service
public class ReviewService {
    ReviewDbStorage reviewDbStorage;
    ReviewLikeDbStorage reviewLikeDbStorage;

    @Autowired
    public ReviewService(ReviewDbStorage reviewDbStorage) {
        this.reviewDbStorage = reviewDbStorage;
    }

    public Review addNewReview(Review review) {
        return reviewDbStorage.addNewReview(review);
    }

    public Review getReviewById(int id) {
        Review review = reviewDbStorage.getReviewById(id).orElseThrow();
        review.setUseful(reviewLikeDbStorage.getUsefulness(id));
        return review;
    }

    public Review updateReview(Review review) {
        getReviewById(review.getReviewId());            // проверяем есть ли в бд ревью, которое хотим обновить
        return reviewDbStorage.update(review);
    }

    public void deleteReview(int id) {
        getReviewById(id);                              // проверяем есть ли в бд искомое по id ревью
        reviewDbStorage.delete(id);
    }

    public Collection<Review> getReviews(Integer filmId, Integer count) {
        return null;
    }

    public Collection<Review> getAllReviews(int countOfReviews) {
        return null;
    }
}
