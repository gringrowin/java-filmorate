package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
       // log.info("findAll: {}", userService.findAll());
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
       // log.info("create: {} - Started", user);
        user = userService.create(user);
      //  log.info("create: {} - Finished", user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
       // log.info("put: {} - Started", user);
        user = userService.put(user);
       // log.info("put: {} - Finished", user);
        return user;
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") Integer id) {
        return userService.getUser(id);
    }

}
