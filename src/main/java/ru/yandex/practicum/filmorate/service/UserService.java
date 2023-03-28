package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
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
        User user = userStorage.getUser(id);
        log.info("getUser: {} - ", user);
        if (user == null) {
            log.warn("user id not found: {}", id);
            throw new UserNotFoundException(String.format(
                    "Пользователя с ID %s не найден.", id));
        }
        return userStorage.getUser(id);
    }

    public User addFriend(Integer userId, Integer friendId) {
        log.info("addFriend: {} - Started", friendId);
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("addFriend: {} - Finished", user);
        return user;
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        log.info("deleteFriend: {} - Started", friendId);
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("deleteFriend: {} - Finished", user);
        return user;
    }

    public List<User> getFriends(Integer userId) {
        log.info("getFriends: {} - ", getUser(userId));
        return getUser(userId)
                .getFriends()
                .stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    public List getCommonFriends(Integer userId, Integer otherId) {

        Set<Integer> userFriends = getUser(userId).getFriends();
        log.info("getCommonFriends: {} - Started", userFriends);
        Set<Integer> otherFriends = getUser(otherId).getFriends();
        log.info("getCommonFriends: {} - Started", otherFriends);
        if (userFriends == null || otherFriends == null) {
            return Collections.EMPTY_LIST;
        }
        Set<Integer> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(otherFriends);
        log.info("getCommonFriends: {} - Finished", commonFriends);
        return commonFriends.stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}
