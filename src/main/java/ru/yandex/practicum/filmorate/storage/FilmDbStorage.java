package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Component
@Qualifier("dbStorage")
public class FilmDbStorage implements FilmStorage {
    @Override
    public Collection<Film> getAll() {
        return null;
    }

    @Override
    public Film add(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film getFilm(Integer id) {
        return null;
    }
}
