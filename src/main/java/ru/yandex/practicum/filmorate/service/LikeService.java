package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.OperationType;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.List;

@Service
@Slf4j
public class LikeService {

    private final LikeStorage likeStorage;

    private final FeedService feedService;

    @Autowired
    public LikeService(LikeStorage likeStorage,
                       FeedService feedService) {
        this.likeStorage = likeStorage;
        this.feedService = feedService;
    }

    public Integer getLikes(Integer filmId) {
        int likes = likeStorage.getLikes(filmId);
        log.info("likes count: {}", likes);
        return likes;
    }

    public void addLike(Integer filmId, Integer userId) {
        log.info("addLike: {} - Started add like to film ID: ", filmId);
        likeStorage.addLike(filmId, userId);
        feedService.addFeedEvent(EventType.LIKE, OperationType.ADD, userId, filmId);
        log.info("addLike: {} - Finished", filmId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        log.info("deleteLike: {} - Started delete like to film ID:", filmId);
        likeStorage.deleteLike(filmId, userId);
        feedService.addFeedEvent(EventType.LIKE, OperationType.REMOVE, userId, filmId);
        log.info("deleteLike: {} - Finished", filmId);
    }

    public List<Integer> getLikedFilmsByUserId(int userId) {
        log.info("getLikedFilmsByUserId: {} - Started:", userId);
        List<Integer> likedFilmes = likeStorage.getLikedFilmsByUserId(userId);
        log.info("getLikedFilmsByUserId: {} - Finished", userId);
        return likedFilmes;
    }
}
