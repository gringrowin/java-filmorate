package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
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
        Film film = filmService.addLike(filmId, userId);
        log.info("addLike: {} - Finished", film);
        return film;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") Integer filmId,
                           @PathVariable("userId") Integer userId) {
        log.info("deleteLike: {} - filmId, {} - userId", filmId, userId);
        Film film = filmService.deleteLike(filmId, userId);
        log.info("deleteLike: {} - Finished", film);
        return film;
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("getPopularFilms: {} - count", count);
        List<Film> popularFilms = filmService.getPopularFilms(count);
        log.info("getPopularFilms: {} - Finished", popularFilms);
        return popularFilms;
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilmsGenreAndYearFiltered(
            @RequestParam(defaultValue = "10", required = false) Integer limit,
            @RequestParam Integer genreId,
            @RequestParam Integer year) {
        log.info("getPopularFilms: {} - count, {} - genreId, {} - year", limit, genreId, year);
        List<Film> popularFilmsSorted = filmService.getPopularFilmsGenreAndYearFiltered(limit, genreId, year);
        log.info("getPopularFilms: {} - Finished", popularFilmsSorted);
        return popularFilmsSorted;
    }
}
