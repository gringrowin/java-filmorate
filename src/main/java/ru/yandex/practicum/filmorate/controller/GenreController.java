package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDbStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreController(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    @GetMapping
    public Collection<Genre> getAll() {
        log.info("getAll - Started");
        Collection<Genre> allGenre = genreDbStorage.getAll();
        log.info("getAll: {} - Finished", allGenre);
        return allGenre;
    }

    @GetMapping("/{genreId}")
    public Genre getGenre(@PathVariable("genreId") Integer genreId) {
        log.info("getGenre - Started");
        Genre genre = genreDbStorage.getGenre(genreId);
        log.info("getGenre: {} - Finished", genre);
        return genre;
    }
}
