package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class FilmRepository {

    private int idGenerator;
    private final Map<Integer, Film> films = new HashMap<>();

    public Collection<Film> getAll() {
        return films.values();
    }

    public void add(Film film) {
        if (film.getId() == null || film.getId() <= 0) {
            film.setId(getIdGenerator());
        }
        if (filmValuesIsValid(film)) {
            films.put(film.getId(), film);
        }
    }

    public void update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Film not found.");
        }
        films.put(film.getId(), film);
    }

    private boolean filmValuesIsValid(Film film) {
        if(films.containsValue(film)) {
            throw new ValidationException("Film with " + film.getName() + " already exists.");
        }
        return true;
    }

    private Integer getIdGenerator() {
        return ++idGenerator;
    }
}
