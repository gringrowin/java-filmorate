package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.*;

@Service
@Slf4j
public class FilmService {


    private final FilmStorage filmStorage;

    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final LikeStorage likeStorage;

    private final UserService userService;

    @Autowired
    public FilmService(@Qualifier("dbFilmStorage") FilmStorage filmStorage,
                       GenreStorage genreStorage,
                       MpaStorage mpaStorage,
                       LikeStorage likeStorage,
                       UserService userService) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.likeStorage = likeStorage;
        this.userService = userService;
    }


    public Collection<Film> findAll() {
        Collection<Film> films = filmStorage.getAll();
        log.info("findAll: {}", films);
        return films;
    }


    public Film create(Film film) {
        log.info("create: {} - Started", film);
        film = filmStorage.add(film);
        log.info("create: {} - Finished", film);
        return film;
    }


    public Film update(Film film) {
        log.info("update: {} - Started", film);
        film = filmStorage.update(film);
        log.info("update: {} - Finished", film);
        return film;
    }

    public Film getFilm(Integer id) {
        Film film = filmStorage.getFilm(id);
        log.info("getFilm: {} - ", film);
        if (film == null) {
            log.error("film id not found: {}", id);
            throw new FilmNotFoundException(String.format(
                    "Фильм с ID %s не найден.", id));
        }
        return film;
    }

    public Film addLike(Integer filmId, Integer userId) {
        checkUserId(userId);
        log.info("addLike: {} - Started add like to film ID: ", filmId);
        Film film = likeStorage.addLike(getFilm(filmId), userId);
        log.info("addLike: {} - Finished", film);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        checkUserId(userId);
        log.info("deleteLike: {} - Started delete like to film ID:", filmId);
        Film film = likeStorage.deleteLike(getFilm(filmId), userId);
        log.info("deleteLike: {} - Finished", film);
        return film;
    }

    public List<Film> getPopularFilms(Integer count) {
        log.info("getPopularFilms: {} - TOP - ", count);
        return findAll().stream().map(film -> film.setLikes(likeStorage.getLikes(film.getId())))
                .sorted(Comparator.comparing(film -> likeStorage.getLikes()).reversed())
                .limit(Objects.requireNonNullElse(count, 10))
                .collect(Collectors.toList());
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
