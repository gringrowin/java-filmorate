package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int idGenerator = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (film.getId() == null || film.getId() <= 0) {
            film.setId(getIdGenerator());
        }
        if(filmValuesIsValid(film)) {
            films.put(film.getId(), film);
        }
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Film not found.");
        }
        films.put(film.getId(), film);

        return film;
    }

    private boolean filmValuesIsValid(Film film) {
        if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Release date must be after 28.12.1895.");
        }
        if(films.containsKey(film.getId())) {
            throw new ValidationException("Film with " + film.getId() + " already exists.");
        }
        return true;
    }

    private Integer getIdGenerator() {
        return idGenerator++;
    }
}
