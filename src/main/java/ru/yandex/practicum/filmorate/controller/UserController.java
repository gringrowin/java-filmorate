package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository = new UserRepository();

    @GetMapping
    public Collection<User> findAll() {
        log.info("findAll: {}", userRepository.size());
        return userRepository.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("create: {}", user);
        userRepository.add(user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        log.info("put: {}", user);
        userRepository.update(user);
        return user;
    }

}
