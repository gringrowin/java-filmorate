package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
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
        return filmStorage.getFilm(id);
    }

    public Film addLike(Integer filmId, Integer userId) {
        filmStorage.getFilm(filmId).getLikes().add(userId);
        return filmStorage.getFilm(filmId);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        filmStorage.getFilm(filmId).getLikes().remove(userId);
        return filmStorage.getFilm(filmId);
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getAll().stream()
                .filter(film -> !film.getLikes().isEmpty())
                .sorted(Comparator.comparingInt(film0 -> film0.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
