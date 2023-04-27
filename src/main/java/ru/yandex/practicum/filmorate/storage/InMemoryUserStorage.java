package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Qualifier("inMemoryStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int idGenerator;
    private final Map<Integer, User> users = new HashMap<>();

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User add(User user) {
        if (users.containsValue(user)) {
            log.warn("user already exists: {}", user);
            throw new ValidationException("User with " + user.getName() + " already exists.");
        }
        setName(user);
        user.setId(getIdGenerator());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        setName(user);
        if (!users.containsKey(user.getId())) {
            log.warn("user not found: {}", user);
            throw new UserNotFoundException("User with id " + user.getId() + " not found.");
        }
        users.put(user.getId(), user);
        return user;
    }

    private void setName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("setNameAndId: {} - setName", user);
        }
    }

    private Integer getIdGenerator() {
        return ++idGenerator;
    }

    public User getUser(Integer id) {
        return users.get(id);
    }
}
