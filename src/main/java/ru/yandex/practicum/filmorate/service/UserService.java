package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("dbUserStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }


    public List<User> findAll() {
        List<User> users = userStorage.getAll();
        log.info("findAll: {}", users);
        return users;
    }

    public User create(User user) {
        checkNameUser(user);
        log.info("create: {} - Started", user);
        user = userStorage.add(user);
        log.info("create: {} - Finished", user);
        return user;
    }

    public User update(User user) {
        checkNameUser(user);
        log.info("update: {} - Started", user);
        user = userStorage.update(user);
        log.info("update: {} - Finished", user);
        return user;
    }

    public User getUser(Integer id) {
        User user = userStorage.getUser(id);
        log.info("getUser: {} - ", user);
        user.setFriends(friendStorage.getFriends(id));
        return user;
    }

    public User addFriend(Integer userId, Integer friendId) {
        log.info("addFriend: {} - Started", friendId);
        User user = friendStorage.addFriend(getUser(userId), getUser(friendId));
        log.info("addFriend: {} - Finished", user);
        return getUser(userId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        log.info("deleteFriend: {} - Started", friendId);
        User user = friendStorage.deleteFriend(getUser(userId), getUser(friendId));
        log.info("deleteFriend: {} - Finished", user);
        return getUser(userId);
    }

    public List<User> getFriends(Integer userId) {
        log.info("getFriends: {} - ", getUser(userId));
        return getUser(userId)
                .getFriends()
                .stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        Set<Integer> userFriends = getUser(userId).getFriends();
        log.info("getCommonFriends: {} - Started", userFriends);
        Set<Integer> otherFriends = getUser(otherId).getFriends();
        log.info("getCommonFriends: {} - Started", otherFriends);
        if (userFriends == null || otherFriends == null) {
            return Collections.emptyList();
        }
        Set<Integer> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(otherFriends);
        log.info("getCommonFriends: {} - Finished", commonFriends);
        return commonFriends.stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    private void checkNameUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
