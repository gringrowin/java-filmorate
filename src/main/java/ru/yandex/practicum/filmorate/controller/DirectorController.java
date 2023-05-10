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
        log.info("Controller command to create director {}", director);
        Director checkDirector = directorService.create(director);
        log.info("Response received {}", checkDirector);
        return checkDirector;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Director updateDirector(@RequestBody @Valid Director director) {
        log.info("Controller command to update director {}", director);
        Director checkDirector = directorService.update(director);
        log.info("Response received {}", checkDirector);
        return checkDirector;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Director getDirector(@PathVariable Integer id) {
        log.info("Controller command to get director by id {}", id);
        Director checkDirector = directorService.get(id);
        log.info("Response received {}", checkDirector);
        return checkDirector;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<Director> getAllDirectors() {
        log.info("Controller command to get all director list");
        Set<Director> checkDirectorSet = directorService.getAll();
        log.info("Response of director set received");
        return checkDirectorSet;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDirector(@PathVariable Integer id) {
        log.info("Controller command to delete director by id {}", id);
        directorService.delete(id);
        log.info("Controller command to delete director by id {} was successful", id);
    }
}
