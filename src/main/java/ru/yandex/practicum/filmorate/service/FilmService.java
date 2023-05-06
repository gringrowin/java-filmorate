package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.FilmSortBy;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidParamsForSearch;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    private final GenreService genreService;
    private final MpaService mpaService;
    private final LikeService likeService;
    private final DirectorService directorService;

    @Autowired
    public FilmService(@Qualifier("dbFilmStorage") FilmStorage filmStorage,
                       GenreService genreService,
                       MpaService mpaService,
                       LikeService likeService,
                       DirectorService directorService) {
        this.filmStorage = filmStorage;
        this.genreService = genreService;
        this.mpaService = mpaService;
        this.likeService = likeService;
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
        genreService.updateGenreByFilmToStorage(film);
        directorService.updateDirectorsByFilmToStorage(film);
        log.info("create: {} - Finished", film);
        return getFilm(film.getId());
    }

    public Film update(Film film) {
        log.info("update: {} - Started", film);
        film = filmStorage.update(film);
        genreService.updateGenreByFilmToStorage(film);
        directorService.updateDirectorsByFilmToStorage(film);
        log.info("update: {} - Finished", film);
        return getFilm(film.getId());
    }

    public Film getFilm(Integer id) {
        Film film = filmStorage.getFilm(id);
        film.setGenres(genreService.getGenresByFilmFromStorage(id));
        film.setLikes(likeService.getLikes(id));
        film.setMpa(mpaService.getMpa(film.getMpa().getId()));
        film.setDirectors(directorService.getDirectorsByFilmFromStorage(film.getId()));
        log.info("getFilm: {} - ", film);
        return film;
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
    public List<Film> searchFilms(String query, String[] paramsForSearch) {
        if (query == null || paramsForSearch == null || paramsForSearch.length > 2) {
            throw new InvalidParamsForSearch("Заданы ошибочные параметры поиска.");
        }
        log.info("Service.searchFilms: {} - query, {} - by", query, paramsForSearch);
        List<Film> findFilms = filmStorage.searchFilms(query, paramsForSearch);
        log.info("Service.searchFilms: {} - Finished", findFilms);

        return addingInfoFilms(findFilms);
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
            film.setGenres(genreService.getGenresByFilmFromStorage(film.getId()));
            film.setLikes(likeService.getLikes(film.getId()));
            film.setMpa(mpaService.getMpa(film.getMpa().getId()));
            film.setDirectors(directorService.getDirectorsByFilmFromStorage(film.getId()));
        }
        return filmList;
    }

    public List<Film> getCommonFilmsForFriendSortedByPopular(Integer userId, Integer friendId) {
//        checkUserId(userId);
//        checkUserId(friendId);
        List<Film> commonFilms = filmStorage.getCommonFilmsForFriendSortedByPopular(userId, friendId);
        log.info("Service getCommonFilmsForFriendSortedByPopular: {} {} {} ", userId, friendId, commonFilms.size());
        return addingInfoFilms(commonFilms);
    }

    public void deleteFilm(Integer filmId) {
        log.info("deleteFilm: {} - ", filmId);
        filmStorage.deleteFilm(filmId);
    }
}
