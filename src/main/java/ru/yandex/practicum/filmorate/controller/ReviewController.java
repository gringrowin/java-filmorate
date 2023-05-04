package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping()
    public Review addNewReview(@Valid @RequestBody Review review) {
        return reviewService.addNewReview(review);
    }

    @PutMapping()
    public Review updateReview(@RequestBody Review review) {
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

    @GetMapping("/{filmId}/{count}")
    public Collection<Review> getReviewsToFilm(@RequestParam Integer filmId, @RequestParam Integer count) {
        return reviewService.getReviews(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public Review addLikeToReview(@PathVariable int id, @PathVariable int userId){
        return reviewService.addLikeToReview(id, userId);
    }

    @PutMapping("/{id}/disLike/{userId}")
    public Review addDisLikeToReview(@PathVariable int id, @PathVariable int userId){
        return reviewService.addDisLikeToReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFromReview(@PathVariable int id, @PathVariable int userId){
        reviewService.deleteLikeFromReview(id, userId);
    }

    @DeleteMapping("/{id}/disLike/{userId}")
    public void deleteDisLikeFromReview(@PathVariable int id, @PathVariable int userId){
        reviewService.deleteLikeFromReview(id, userId);
    }


}
