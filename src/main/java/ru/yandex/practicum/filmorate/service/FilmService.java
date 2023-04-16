package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;


    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
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
        Film film = getFilm(filmId);
        log.info("addLike: {} - Started", film);
        film.getLikes().add(userId);
        log.info("addLike: {} - Finished", film);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        checkUserId(userId);
        Film film = getFilm(filmId);
        log.info("deleteLike: {} - Started", film);
        film.getLikes().remove(userId);
        log.info("deleteLike: {} - Finished", film);
        return film;
    }

    public List<Film> getPopularFilms(Integer count) {
        log.info("getPopularFilms: {} - TOP - ", count);
        return findAll().stream()
                .sorted(Comparator.comparing(Film::getLikesCount).reversed())
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
