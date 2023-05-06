package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dao.ReviewDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.ReviewLikeDbStorage;

import java.util.Collection;

@Slf4j
@Service
public class ReviewService {
    ReviewDbStorage reviewDbStorage;
    ReviewLikeDbStorage reviewLikeDbStorage;
    UserService userService;

    @Autowired
    public ReviewService(ReviewDbStorage reviewDbStorage,
                         ReviewLikeDbStorage reviewLikeDbStorage,
                         UserService userService) {
        this.reviewDbStorage = reviewDbStorage;
        this.reviewLikeDbStorage = reviewLikeDbStorage;
        this.userService = userService;
    }

    public Review addNewReview(Review review) {
        Review newReview = reviewDbStorage.addNewReview(review);
        log.info("Добавлен новый отзыв " + newReview);
        return newReview;
    }

    public Review getReviewById(int id) {
        Review review = reviewDbStorage.getReviewById(id).get();
        review.setUseful(reviewLikeDbStorage.getUsefulness(id));
        return review;
    }

    public Review updateReview(Review review) {
        return reviewDbStorage.update(review);
    }

    public void deleteReview(int id) {
        reviewDbStorage.delete(id);
    }

    public Collection<Review> getReviews(Integer filmId, Integer count) {
        return reviewDbStorage.getReviews(filmId, count);
    }

    public void addLikeToReview(int reviewId, int userId) {
        reviewLikeDbStorage.addReaction(reviewId, userId, true);
    }

    public void addDisLikeToReview(int reviewId, int userId) {
        reviewLikeDbStorage.addReaction(reviewId, userId, false);
    }

    public void deleteLikeFromReview(int reviewId, int userId) {
        getReviewById(reviewId);
        userService.getUser(userId);
        reviewLikeDbStorage.deleteReaction(reviewId, userId);
    }
}
