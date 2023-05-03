package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.storage.dao.ReviewLikeDbStorage;

public class ReviewLikesService {
    ReviewLikeDbStorage reviewLikeDbStorage;

    @Autowired
    public ReviewLikesService(ReviewLikeDbStorage reviewLikeDbStorage) {
        this.reviewLikeDbStorage = reviewLikeDbStorage;
    }
}
