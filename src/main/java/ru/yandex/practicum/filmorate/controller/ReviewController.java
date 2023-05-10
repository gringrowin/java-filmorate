package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping()
    public Review addNewReview(@Valid @RequestBody Review review) {
        log.info("Получен запрос на добавление нового отзыва " + review);
        review = reviewService.addNewReview(review);
        log.info("addNewReview: {} - Finished", review);
        return review;
    }

    @PutMapping()
    public Review updateReview(@Valid @RequestBody Review review) {
        log.info("Получен запрос на обновление отзыва " + review);
        review = reviewService.updateReview(review);
        log.info("addNewReview: {} - Finished", review);
        return review;
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable int id) {
        log.info("Получен запрос на удаление " + getReview(id));
        reviewService.deleteReview(id);
    }

    @GetMapping("/{id}")
    public Review getReview(@PathVariable int id) {
        log.info("Получен запрос на получение отзыва по id " + id);
        Review review = reviewService.getReview(id);
        log.info("getReview: {} - Finished", review);
        return review;
    }

    @GetMapping
    public Collection<Review> getReviewsToFilm(
            @RequestParam(required = false) Integer filmId,
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Получен запрос на поиск отзывов на фильм с id={} в количестве {}", filmId, count);
        Collection<Review> collectionReviews = reviewService.getReviews(filmId, count);
        log.info("getReviewsToFilm: {} - Finished", collectionReviews);
        return collectionReviews;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToReview(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос на добавление лайка на отзыв с id={} от пользователя с id={}", id, userId);
        reviewService.addLikeToReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDisLikeToReview(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос на добавление дизлайка на отзыв с id={} от пользователя с id={}", id, userId);
        reviewService.addDisLikeToReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFromReview(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос на удаление лайка на отзыв с id={} от пользователя с id={}", id, userId);
        reviewService.deleteLikeFromReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDisLikeFromReview(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос на удаление дизлайка на отзыв с id={} от пользователя с id={}", id, userId);
        reviewService.deleteLikeFromReview(id, userId);
    }
}
