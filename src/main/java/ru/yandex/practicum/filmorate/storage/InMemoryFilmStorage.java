package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Qualifier("inMemoryStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private int idGenerator;
    private final Map<Integer, Film> films = new HashMap<>();

    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    public Film add(Film film) {
        if (films.containsValue(film)) {
            log.warn("film already exists: {}", film);
            throw new ValidationException("Film with " + film.getName() + " already exists.");
        }
        if (film.getId() == null || film.getId() <= 0) {
            film.setId(getIdGenerator());
            log.info("add: {} - setId", film);
        }
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("film not found: {}", film);
            throw new FilmNotFoundException("Film not found.");
        }
        films.put(film.getId(), film);
        return film;
    }

    public Film getFilm(Integer id) {
        return films.get(id);
    }

    private Integer getIdGenerator() {
        return ++idGenerator;
    }
}
