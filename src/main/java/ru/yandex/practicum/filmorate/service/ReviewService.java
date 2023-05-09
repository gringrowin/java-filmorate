package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.OperationType;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dao.ReviewDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.ReviewLikeDbStorage;

import java.util.Collection;

@Slf4j
@Service
public class ReviewService {
    private final ReviewDbStorage reviewDbStorage;
    private final ReviewLikeDbStorage reviewLikeDbStorage;
    private final UserService userService;

    private final FeedService feedService;

    @Autowired
    public ReviewService(ReviewDbStorage reviewDbStorage,
                         ReviewLikeDbStorage reviewLikeDbStorage,
                         UserService userService,
                         FeedService feedService) {
        this.reviewDbStorage = reviewDbStorage;
        this.feedService = feedService;
        this.reviewLikeDbStorage = reviewLikeDbStorage;
        this.userService = userService;
    }

    public Review addNewReview(Review review) {
        idValidation(review);
        Review newReview = reviewDbStorage.addNewReview(review);
        feedService.addFeedEvent(EventType.REVIEW, OperationType.ADD, review.getUserId(), review.getReviewId());
        log.info("Добавлен новый отзыв " + newReview);
        return newReview;
    }

    public Review getReviewById(int id) {
        Review review = reviewDbStorage.getReviewById(id).get();
        review.setUseful(reviewLikeDbStorage.getUsefulness(id));
        return review;
    }

    public Review updateReview(Review review) {
        Review updatedReview = reviewDbStorage.update(review);
        feedService.addFeedEvent(EventType.REVIEW, OperationType.UPDATE, updatedReview.getUserId(), updatedReview.getReviewId());
        return updatedReview;
    }

    public void deleteReview(int id) {
        Review review = getReviewById(id);
        reviewDbStorage.delete(id);
        feedService.addFeedEvent(EventType.REVIEW, OperationType.REMOVE, review.getUserId(), review.getReviewId());
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
        reviewLikeDbStorage.deleteReaction(reviewId, userId);
    }

    private void idValidation(Review review) {
        if (review.getUserId() < 1) {
            throw new UserNotFoundException("id пользователя не может быть меньше 1");
        }
        if (review.getFilmId() < 1) {
            throw new FilmNotFoundException("id фильма не может быть меньше 1");
        }
    }

}
