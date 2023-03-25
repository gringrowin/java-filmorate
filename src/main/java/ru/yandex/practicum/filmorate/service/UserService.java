package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.Collection;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public Collection<User> findAll() {
        log.info("findAll: {}", userStorage.size());
        return userStorage.getAll();
    }


    public User create(User user) {
        log.info("create: {} - Started", user);
        user = userStorage.add(user);
        log.info("create: {} - Finished", user);
        return user;
    }

    public User put(User user) {
        log.info("put: {} - Started", user);
        user = userStorage.update(user);
        log.info("put: {} - Finished", user);
        return user;
    }

    public User getUser(Integer id) {
        return userStorage.getUser(id);
    }
}
