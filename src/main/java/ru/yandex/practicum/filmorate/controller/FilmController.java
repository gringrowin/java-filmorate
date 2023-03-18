package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmRepository;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmRepository filmRepository = new FilmRepository();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("findAll: {}", filmRepository.size());
        return filmRepository.getAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("create: {} - Started", film);
        film = filmRepository.add(film);
        log.info("create: {} - Finished", film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        log.info("put: {} - Started", film);
        film = filmRepository.update(film);
        log.info("put: {} - Finished", film);
        return film;
    }
}
