package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    public Collection<Film> findAll() {
        log.info("findAll: {}", filmStorage.size());
        return filmStorage.getAll();
    }


    public Film create(Film film) {
        log.info("create: {} - Started", film);
        film = filmStorage.add(film);
        log.info("create: {} - Finished", film);
        return film;
    }


    public Film put(Film film) {
        log.info("put: {} - Started", film);
        film = filmStorage.update(film);
        log.info("put: {} - Finished", film);
        return film;
    }

    public Film getFilm(Integer id) {
        Film film = filmStorage.getFilm(id);
        if (film == null) {
            throw new FilmNotFoundException(String.format(
                    "Фильм с ID %s не найден.", id));
        }
        return film;
    }

    public Film addLike(Integer filmId, Integer userId) {
        checkUserId(userId);
        Film film = getFilm(filmId);
        film.getLikes().add(userId);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        checkUserId(userId);
        Film film = getFilm(filmId);
        film.getLikes().remove(userId);
        return film;
    }

    public List<Film> getPopularFilms(Integer count) {
        return findAll().stream()
                .sorted(Comparator.comparing(Film::getLikesCount).reversed())
                .limit(Objects.requireNonNullElse(count, 10))
                .collect(Collectors.toList());
    }

    private void checkUserId(Integer userId) {
        HashMap<String, Integer> params = new HashMap<>();
        params.put("userId", userId);
        try {
            ResponseEntity<User> response
                    = new RestTemplate().getForEntity(
                    "http://localhost:8080/users/{userId}",
                    User.class, params);
        }
        catch (Exception ex) {
            throw new UserNotFoundException(String.format(
                    "Пользователя с ID %s не найден.", userId));
        }
    }
}
