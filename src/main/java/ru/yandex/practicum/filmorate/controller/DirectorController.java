package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Director createDirector(@RequestBody @Valid Director director) {
        log.info("Creating director {} was started", director);
        Director checkDirector = directorService.create(director);
        log.info("Creating director {} was successfully finished", checkDirector);
        return checkDirector;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Director updateDirector(@RequestBody @Valid Director director) {
        log.info("Updating director {} was started", director);
        Director checkDirector = directorService.update(director);
        log.info("Updating director {} was successfully finished", checkDirector);
        return checkDirector;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Director getDirector(@PathVariable Integer id) {
        log.info("Getting director by id {} was started", id);
        Director checkDirector = directorService.get(id);
        log.info("Getting director by id {} was successfully finished", checkDirector);
        return checkDirector;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<Director> getAllDirectors() {
        log.info("Getting all director set was started");
        Set<Director> checkDirectorSet = directorService.getAll();
        log.info("Getting of director set was successfully finished");
        return checkDirectorSet;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDirector(@PathVariable Integer id) {
        log.info("Deletion of director by id {} was started", id);
        directorService.delete(id);
        log.info("Deletion of director by id {} was successfully finished", id);
    }
}
