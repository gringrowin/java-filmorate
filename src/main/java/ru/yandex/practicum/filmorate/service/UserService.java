package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;

@Service
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();


    public Collection<User> findAll() {
        log.info("findAll: {}", inMemoryUserStorage.size());
        return inMemoryUserStorage.getAll();
    }


    public User create(@Valid @RequestBody User user) {
        log.info("create: {} - Started", user);
        user = inMemoryUserStorage.add(user);
        log.info("create: {} - Finished", user);
        return user;
    }

    public User put(@Valid @RequestBody User user) {
        log.info("put: {} - Started", user);
        user = inMemoryUserStorage.update(user);
        log.info("put: {} - Finished", user);
        return user;
    }
}
