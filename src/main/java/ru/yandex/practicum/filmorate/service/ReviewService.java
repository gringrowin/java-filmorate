package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
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
//        if (review.getUserId() < 1) {
//            throw new UserNotFoundException("id пользователя не может быть меньше 1");
//        }
//        if (review.getFilmId() < 1) {
//            throw new FilmNotFoundException("id фильма не может быть меньше 1");
//        }
        reviewDbStorage.addNewReview(review);
        Review newReview = getReviewById(review.getReviewId());
        log.info("Добавлен новый отзыв " + newReview);
        return newReview;
    }

    public Review getReviewById(int id) {
        Review review = reviewDbStorage.getReviewById(id).get();
        review.setUseful(reviewLikeDbStorage.getUsefulness(id));
        return review;
    }

    public Review updateReview(Review review) {
        getReviewById(review.getReviewId());
        return reviewDbStorage.update(review);
    }

    public void deleteReview(int id) {
        getReviewById(id);
        reviewDbStorage.delete(id);
    }

    public Collection<Review> getReviews(Integer filmId, Integer count) {
        return reviewDbStorage.getReviews(filmId, count);
    }

    public void addLikeToReview(int reviewId, int userId) {
        reviewLikeDbStorage.addLike(reviewId, userId, true);
    }

    public void addDisLikeToReview(int reviewId, int userId) {
        reviewLikeDbStorage.addLike(reviewId, userId, false);
    }

    public void deleteLikeFromReview(int reviewId, int userId) {
        getReviewById(reviewId);
        userService.getUser(userId);
        reviewLikeDbStorage.deleteLike(reviewId, userId);
    }
}
