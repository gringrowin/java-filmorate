package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class FilmRepository {

    private int idGenerator;
    private final Map<Integer, Film> films = new HashMap<>();

    public Collection<Film> getAll() {
        return films.values();
    }

    public Film add(Film film) {
        if(films.containsValue(film)) {
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
            throw new ValidationException("Film not found.");
        }
        films.put(film.getId(), film);
        return film;
    }

    private Integer getIdGenerator() {
        return ++idGenerator;
    }

    public int size() {
        return films.size();
    }
}
