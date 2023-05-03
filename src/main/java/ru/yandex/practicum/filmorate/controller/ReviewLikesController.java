package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewLikesService;

@RestController
@RequestMapping("/reviews")
public class ReviewLikesController {
    private ReviewLikesService reviewLikesService;

    @Autowired
    public ReviewLikesController(ReviewLikesService reviewLikesService) {
        this.reviewLikesService = reviewLikesService;
    }

    @PutMapping("/{id}/like/{userId}")
    public Review addLikeToReview(@PathVariable int id, @PathVariable int userId){
        return reviewLikesService.addLikeToReview(id, userId);
    }

    @PutMapping("/{id}/disLike/{userId}")
    public Review addDisLikeToReview(@PathVariable int id, @PathVariable int userId){
        return reviewLikesService.addDisLikeToReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Review deleteLikeFromReview(@PathVariable int id, @PathVariable int userId){
        return reviewLikesService.deleteLikeFromReview(id, userId);
    }

    @DeleteMapping("/{id}/disLike/{userId}")
    public Review deleteDisLikeFromReview(@PathVariable int id, @PathVariable int userId){
        return reviewLikesService.deleteDisLikeFromReview(id, userId);
    }
}
