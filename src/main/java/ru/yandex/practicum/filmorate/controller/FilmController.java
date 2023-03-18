package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("findAll: {}", inMemoryFilmStorage.size());
        return inMemoryFilmStorage.getAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("create: {} - Started", film);
        film = inMemoryFilmStorage.add(film);
        log.info("create: {} - Finished", film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        log.info("put: {} - Started", film);
        film = inMemoryFilmStorage.update(film);
        log.info("put: {} - Finished", film);
        return film;
    }
}
