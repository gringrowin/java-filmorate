package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Service
@Slf4j
public class LikeService {

    private final FilmService filmService;

    private final LikeStorage likeStorage;

    private final UserService userService;

    @Autowired
    public LikeService(FilmService filmService,
                       LikeStorage likeStorage,
                       UserService userService) {
        this.filmService = filmService;
        this.likeStorage = likeStorage;
        this.userService = userService;
    }

    public Integer getLikes(Integer filmId) {
        int likes = likeStorage.getLikes(filmId);
        log.info("likes count: {}", likes);
        return likes;
    }

    public Film addLike(Integer filmId, Integer userId) {
        checkUserId(userId);
        log.info("addLike: {} - Started add like to film ID: ", filmId);
        Film film = likeStorage.addLike(filmService.getFilm(filmId), userId);
        log.info("addLike: {} - Finished", film);
        return filmService.getFilm(filmId);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        checkUserId(userId);
        log.info("deleteLike: {} - Started delete like to film ID:", filmId);
        Film film = likeStorage.deleteLike(filmService.getFilm(filmId), userId);
        log.info("deleteLike: {} - Finished", film);
        return filmService.getFilm(filmId);
    }

    private void checkUserId(Integer userId) {
        log.info("checkUserId: {} - ", userId);
        User user = userService.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователя с ID %s не найден.", userId));
        }
    }

}
