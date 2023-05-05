package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        return reviewService.addNewReview(review);
    }

    @PutMapping()
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable int id) {
        reviewService.deleteReview(id);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable int id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping
    public Collection<Review> getReviewsToFilm(@RequestParam(required = false) Integer filmId,
                                               @RequestParam(required = false) Integer count) {
        log.info("Получен запрос на поиск отзывов на фильм с id={} в количестве {}", filmId, count);
        return reviewService.getReviews(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToReview(@PathVariable int id, @PathVariable int userId){
        log.trace("Получен запрос на добавление лайка на отзыв с id={} от пользователя с id={}", id, userId);
        reviewService.addLikeToReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDisLikeToReview(@PathVariable int id, @PathVariable int userId){
        log.trace("Получен запрос на добавление дизлайка на отзыв с id={} от пользователя с id={}", id, userId);
        reviewService.addDisLikeToReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFromReview(@PathVariable int id, @PathVariable int userId){
        reviewService.deleteLikeFromReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDisLikeFromReview(@PathVariable int id, @PathVariable int userId){
        reviewService.deleteLikeFromReview(id, userId);
    }


}
