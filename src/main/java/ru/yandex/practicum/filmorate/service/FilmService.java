package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InvalidParamsForSearch;
import ru.yandex.practicum.filmorate.enums.FilmSortBy;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final LikeStorage likeStorage;

    private final UserService userService;
    private final DirectorService directorService;

    @Autowired
    public FilmService(@Qualifier("dbFilmStorage") FilmStorage filmStorage,
                       GenreStorage genreStorage,
                       MpaStorage mpaStorage,
                       LikeStorage likeStorage,
                       UserService userService,
                       DirectorService directorService) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.likeStorage = likeStorage;
        this.userService = userService;
        this.directorService = directorService;
    }

    public List<Film> findAll() {
        List<Film> films = filmStorage.getAll();
        log.info("findAll: {}", films);
        return addingInfoFilms(films);
    }

    public Film create(Film film) {
        log.info("create: {} - Started", film);
        film = filmStorage.add(film);
        genreStorage.updateGenreByFilmToStorage(film);
        directorService.updateDirectorsByFilmToStorage(film);
        log.info("create: {} - Finished", film);
        return getFilm(film.getId());
    }

    public Film update(Film film) {
        log.info("update: {} - Started", film);
        film = filmStorage.update(film);
        genreStorage.updateGenreByFilmToStorage(film);
        directorService.updateDirectorsByFilmToStorage(film);
        log.info("update: {} - Finished", film);
        return getFilm(film.getId());
    }

    public Film getFilm(Integer id) {
        Film film = filmStorage.getFilm(id);
        film.setGenres(genreStorage.getGenresByFilmFromStorage(id));
        film.setLikes(likeStorage.getLikes(id));
        film.setMpa(mpaStorage.getMpa(film.getMpa().getId()));
        film.setDirectors(directorService.getDirectorsByFilmFromStorage(film.getId()));
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
        popularFilms = filmStorage.getPopularFilms(count, genreId, year);
        addingInfoFilms(popularFilms);
        return popularFilms;
    }

    public List<Film> getFilmsByDirectorIdAndSort(Integer directorId, FilmSortBy sortBy) {
        checkDirectorId(directorId);
        log.info("Service command to get directors (id: {}) sorted by {}", directorId, sortBy);
        List<Film> films = filmStorage.getFilmsByDirectorIdAndSort(directorId, sortBy);
        return addingInfoFilms(films);
    }

    private void checkUserId(Integer userId) {
        log.info("checkUserId: {} - ", userId);
        User user = userService.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь с ID %s не найден.", userId));
        }
    }

    public List<Film> searchFilms(String query, String[] paramsForSearch) {
        if (query == null || paramsForSearch == null || paramsForSearch.length > 2) {
            throw new InvalidParamsForSearch("Заданы ошибочные параметры поиска.");
        }
        log.info("Service.searchFilms: {} - query, {} - by", query, paramsForSearch);
        List<Film> findFilms = filmStorage.searchFilms(query, paramsForSearch);
        log.info("Service.searchFilms: {} - Finished", findFilms);

        return findFilms;
    }

    private void checkDirectorId(Integer directorId) {
        log.info("checkDirectorId - {}", directorId);
        Director director = directorService.getById(directorId);
        if (director == null) {
            log.error("Director with id {} is not found!", directorId);
            throw new DirectorNotFoundException(String.format("Director with id %s is not found!", directorId));
        }
    }

    private List<Film> addingInfoFilms(List<Film> filmList) {
        for (Film film : filmList) {
            film.setGenres(genreStorage.getGenresByFilmFromStorage(film.getId()));
            film.setLikes(likeStorage.getLikes(film.getId()));
            film.setMpa(mpaStorage.getMpa(film.getMpa().getId()));
            film.setDirectors(directorService.getDirectorsByFilmFromStorage(film.getId()));
        }
        return filmList;
    }
}
