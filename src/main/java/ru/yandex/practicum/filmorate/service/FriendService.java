package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FriendService {

    private final UserService userService;
    private final FriendStorage friendStorage;

    @Autowired
    public FriendService(UserService userService,
                         FriendStorage friendStorage) {
        this.userService = userService;
        this.friendStorage = friendStorage;
    }

    public User addFriend(Integer userId, Integer friendId) {
        log.info("addFriend: {} - Started", friendId);
        User user = friendStorage.addFriend(userService.getUser(userId), userService.getUser(friendId));
        log.info("addFriend: {} - Finished", user);
        return userService.getUser(userId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        log.info("deleteFriend: {} - Started", friendId);
        User user = friendStorage.deleteFriend(userService.getUser(userId), userService.getUser(friendId));
        log.info("deleteFriend: {} - Finished", user);
        return userService.getUser(userId);
    }

    public List<User> getFriends(Integer userId) {
        log.info("getFriends: {} - ", userService.getUser(userId));
        return userService.getUser(userId)
                .getFriends()
                .stream()
                .map(userService::getUser)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        Set<Integer> userFriends = userService.getUser(userId).getFriends();
        log.info("getCommonFriends: {} - Started", userFriends);
        Set<Integer> otherFriends = userService.getUser(otherId).getFriends();
        log.info("getCommonFriends: {} - Started", otherFriends);
        if (userFriends == null || otherFriends == null) {
            return Collections.emptyList();
        }
        Set<Integer> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(otherFriends);
        log.info("getCommonFriends: {} - Finished", commonFriends);
        return commonFriends.stream()
                .map(userService::getUser)
                .collect(Collectors.toList());
    }
}
