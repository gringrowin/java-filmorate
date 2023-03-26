package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public User addFriend(Integer userId, Integer friendId) {
        userStorage.getUser(userId).getFriends().add(friendId);
        userStorage.getUser(friendId).getFriends().add(userId);
        return userStorage.getUser(userId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        userStorage.getUser(userId).getFriends().remove(friendId);
        userStorage.getUser(friendId).getFriends().remove(userId);
        return userStorage.getUser(userId);
    }

    public List<User> getFriends(Integer userId) {
        return userStorage
                .getUser(userId)
                .getFriends()
                .stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        Set<Integer> userFriends = userStorage.getUser(userId).getFriends();
        Set<Integer> otherFriends = userStorage.getUser(userId).getFriends();
        userFriends.retainAll(otherFriends);
        return userFriends.stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}
