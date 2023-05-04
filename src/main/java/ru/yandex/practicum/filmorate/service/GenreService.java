package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.Set;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> getAll() {
        Collection<Genre> genres = genreStorage.getAll();
        log.info("findAll: {}", genres);
        return genres;
    }

    public Genre getGenre(Integer genreId) {
        Genre genre = genreStorage.getGenre(genreId);
        log.info("getGenre: {} - ", genre);
        return genre;
    }

    public void updateGenreByFilmToStorage(Film film) {
        log.info("update: {}", film);
        genreStorage.updateGenreByFilmToStorage(film);
    }

    public Set<Genre> getGenresByFilmFromStorage(Integer filmId) {
        Set<Genre> genres = genreStorage.getGenresByFilmFromStorage(filmId);
        log.info("getGenresByFilmFromStorage: {}", genres);
        return genres;
    }
}
