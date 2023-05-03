package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.Set;

@Service
@Slf4j
public class DirectorService {

    private final DirectorStorage directorStorage;

    @Autowired
    public DirectorService(@Qualifier("dbDirectorStorage") DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public Director create(Director director) {
        log.info("Service command to create director: {}", director);
        return directorStorage.create(director);
    }

    public Director update(Director director) {
        log.info("Service command to update director: {}", director);
        if (directorStorage.getById(director.getId()) == null) {
            log.error("Director with id {} is not found!", director.getId());
            throw new DirectorNotFoundException(String.format("Director with id %s is not found!", director.getId()));
        }
        return directorStorage.update(director);
    }

    public Director getById(Integer directorId) {
        Director director = directorStorage.getById(directorId);
        if (director == null) {
            log.error("Director with id {} is not found!", directorId);
            throw new DirectorNotFoundException(String.format("Director with id %s is not found!", directorId));
        }
        return director;
    }

    public Set<Director> getAll() {
        log.info("Service command by getting all directors");
        return directorStorage.getAll();
    }

    public void delete(Integer directorId) {
        boolean isDirectorDeleted = directorStorage.delete(directorId);
        if (!isDirectorDeleted) {
            log.error("Director with id {} is not found!", directorId);
            throw new DirectorNotFoundException(String.format("Director with id %s is not found!", directorId));
        }
        log.info("Service command to delete director by id {}", directorId);
    }

    public void updateDirectorsByFilmToStorage(Film film) {
        log.info("Service command to update director list by id of film {}", film.getId());
        directorStorage.updateDirectorsByFilmToStorage(film);
    }

    public Set<Director> getDirectorsByFilmFromStorage(Integer filmId) {
        log.info("Service command to update director list by id of film {}", filmId);
        return directorStorage.getDirectorsByFilmFromStorage(filmId);
    }
}
