package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private int idGenerator = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if(user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getId() == null || user.getId() <= 0) {
            user.setId(getIdGenerator());
        }
        if(userValuesIsValid(user)) {
            users.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("User not found.");
        }
        if(user.getName() == null) {
            user.setName(user.getLogin());
        }
        if(userValuesIsValid(user)) {
            users.put(user.getId(), user);
        }
        return user;
    }

    private boolean userValuesIsValid(User user) {
        if(user.getLogin().contains(" ")) {
            throw new ValidationException("The login must not contain whitespaces.");
        }
        return true;
    }

    private Integer getIdGenerator() {
        return idGenerator++;
    }
}
