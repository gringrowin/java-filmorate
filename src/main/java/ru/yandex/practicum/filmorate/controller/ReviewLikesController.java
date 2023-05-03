package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.ReviewLikesService;

@RestController
@RequestMapping("/reviews")
public class ReviewLikesController {
    private ReviewLikesService reviewLikesService;

    @Autowired
    public ReviewLikesController(ReviewLikesService reviewLikesService) {
        this.reviewLikesService = reviewLikesService;
    }
}
