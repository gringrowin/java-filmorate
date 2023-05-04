package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Review addNewReview(@Valid @RequestBody Review review) {
        return reviewService.addNewReview(review);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReview(@PathVariable int id) {
        reviewService.deleteReview(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Review getReviewById(@PathVariable int id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping("/{filmId}/{count}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Review> getReviewsToFilm(@RequestParam Integer filmId, @RequestParam Integer count) {
        return reviewService.getReviews(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Review addLikeToReview(@PathVariable int id, @PathVariable int userId){
        return reviewService.addLikeToReview(id, userId);
    }

    @PutMapping("/{id}/disLike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Review addDisLikeToReview(@PathVariable int id, @PathVariable int userId){
        return reviewService.addDisLikeToReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLikeFromReview(@PathVariable int id, @PathVariable int userId){
        reviewService.deleteLikeFromReview(id, userId);
    }

    @DeleteMapping("/{id}/disLike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDisLikeFromReview(@PathVariable int id, @PathVariable int userId){
        reviewService.deleteLikeFromReview(id, userId);
    }


}
