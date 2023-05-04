package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.enums.FilmSortBy;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final LikeService likeService;

    public FilmController(FilmService filmService,
                          LikeService likeService) {
        this.filmService = filmService;
        this.likeService = likeService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("findAll - Started");
        Collection<Film> allFilms = filmService.findAll();
        log.info("findAll: {} - Finished", allFilms);
        return allFilms;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("create: {} - Started", film);
        film = filmService.create(film);
        log.info("create: {} - Finished", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("update: {} - Started", film);
        film = filmService.update(film);
        log.info("update: {} - Finished", film);
        return film;
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable("filmId") Integer filmId) {
        log.info("getFilm: {} - Started", filmId);
        Film film = filmService.getFilm(filmId);
        log.info("getFilm: {} - Finished", film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") Integer filmId,
                        @PathVariable("userId") Integer userId) {
        log.info("addLike: {} - filmId, {} - userId", filmId, userId);
        Film film = likeService.addLike(filmId, userId);
        log.info("addLike: {} - Finished", film);
        return film;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") Integer filmId,
                           @PathVariable("userId") Integer userId) {
        log.info("deleteLike: {} - filmId, {} - userId", filmId, userId);
        Film film = likeService.deleteLike(filmId, userId);
        log.info("deleteLike: {} - Finished", film);
        return film;
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(defaultValue = "10", required = false) Integer count,
            @RequestParam(required = false) Integer genreId,
            @RequestParam(required = false) @Min(1895) Integer year) {
        log.info("getPopularFilms: {} - count, {} - genreId, {} - year", count, genreId, year);
        List<Film> popularFilms = filmService.getPopularFilms(count, genreId, year);
        log.info("getPopularFilms: {} - Finished", popularFilms);
        return popularFilms;
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirectorIdAndSort(@PathVariable int directorId, @RequestParam FilmSortBy sortBy) {
        log.info("Command of FilmController to get sorted director film list");
        return filmService.getFilmsByDirectorIdAndSort(directorId, sortBy);
    }
}
