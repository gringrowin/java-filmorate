package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.Collection;

@Service
public class FilmService {

    private final FilmStorage inMemoryFilmStorage;


    public Collection<Film> findAll() {
        log.info("findAll: {}", inMemoryFilmStorage.size());
        return inMemoryFilmStorage.getAll();
    }


    public Film create(@Valid @RequestBody Film film) {
        log.info("create: {} - Started", film);
        film = inMemoryFilmStorage.add(film);
        log.info("create: {} - Finished", film);
        return film;
    }


    public Film put(@Valid @RequestBody Film film) {
        log.info("put: {} - Started", film);
        film = inMemoryFilmStorage.update(film);
        log.info("put: {} - Finished", film);
        return film;
    }
}
