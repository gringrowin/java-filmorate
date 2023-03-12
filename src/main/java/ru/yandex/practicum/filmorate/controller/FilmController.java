package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
        log.info("create: {}", film);
        filmRepository.add(film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        log.info("put: {}", film);
        filmRepository.update(film);
        return film;
    }
}
