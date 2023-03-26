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
        if (user == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователя с ID %s не найден.", id));
        }
        return userStorage.getUser(id);
    }

    public User addFriend(Integer userId, Integer friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        return getUser(userId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        return getUser(userId);
    }

    public List<User> getFriends(Integer userId) {
        return getUser(userId)
                .getFriends()
                .stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    public List getCommonFriends(Integer userId, Integer otherId) {
        Set<Integer> userFriends = getUser(userId).getFriends();
        Set<Integer> otherFriends = getUser(otherId).getFriends();
        if (userFriends == null || otherFriends == null) {
            return Collections.EMPTY_LIST;
        }
        Set<Integer> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(otherFriends);
        return commonFriends.stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}
