package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;

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
}
