package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<Film> findAll() {
        List<Film> films = filmStorage.getAll();
        for (Film film : films) {
            film.setGenres(genreStorage.getGenresByFilmFromStorage(film.getId()));
            film.setLikes(likeStorage.getLikes(film.getId()));
            film.setMpa(mpaStorage.getMpa(film.getMpa().getId()));
        }
        log.info("findAll: {}", films);
        return films;
    }

    public Film create(Film film) {
        log.info("create: {} - Started", film);
        film = filmStorage.add(film);
        genreStorage.updateGenreByFilmToStorage(film);
        log.info("create: {} - Finished", film);
        return getFilm(film.getId());
    }

    public Film update(Film film) {
        log.info("update: {} - Started", film);
        film = filmStorage.update(film);
        genreStorage.updateGenreByFilmToStorage(film);
        log.info("update: {} - Finished", film);
        return getFilm(film.getId());
    }

    public Film getFilm(Integer id) {
        Film film = filmStorage.getFilm(id);
        film.setGenres(genreStorage.getGenresByFilmFromStorage(id));
        film.setLikes(likeStorage.getLikes(id));
        film.setMpa(mpaStorage.getMpa(film.getMpa().getId()));
        log.info("getFilm: {} - ", film);
        return film;
    }

    public Film addLike(Integer filmId, Integer userId) {
        checkUserId(userId);
        log.info("addLike: {} - Started add like to film ID: ", filmId);
        Film film = likeStorage.addLike(getFilm(filmId), userId);
        log.info("addLike: {} - Finished", film);
        return getFilm(filmId);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        checkUserId(userId);
        log.info("deleteLike: {} - Started delete like to film ID:", filmId);
        Film film = likeStorage.deleteLike(getFilm(filmId), userId);
        log.info("deleteLike: {} - Finished", film);
        return getFilm(filmId);
    }

    public List<Film> getPopularFilms(Integer count, Integer genreId, Integer year) {
        List<Film> popularFilms;
        log.info("getPopularFilms: {} - TOP, {} - genreId, {} - year - ", count, genreId, year);
        if (genreId != null) {
            genreStorage.getGenre(genreId);
        }
        if (genreId == null && year == null) {
            popularFilms = findAll();
        } else {
            popularFilms = filmStorage.getPopularFilms(genreId, year);
        }
        for (Film film : popularFilms) {
            film.setGenres(genreStorage.getGenresByFilmFromStorage(film.getId()));
            film.setLikes(likeStorage.getLikes(film.getId()));
            film.setMpa(mpaStorage.getMpa(film.getMpa().getId()));
        }
        Stream<Film> filmsStream = popularFilms.stream()
                .sorted(Comparator.comparing(Film::getLikes).reversed())
                .limit(Objects.requireNonNullElse(count, 10));
        popularFilms = filmsStream.collect(Collectors.toList());
        filmsStream.close();
        return popularFilms;
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
