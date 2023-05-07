package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.*;

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
        List<User> friends = new ArrayList<>();
        for (Integer friendId : friendStorage.getFriends(userId)) {
            friends.add(userService.getUser(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        Set<User> userFriends = new HashSet<>(getFriends(userId));
        log.info("getCommonFriends: {} - Started", userFriends);
        Set<User> otherFriends = new HashSet<>(getFriends(otherId));
        log.info("getCommonFriends: {} - Started", otherFriends);
        if (userFriends == null || otherFriends == null) {
            return Collections.emptyList();
        }
        Set<User> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(otherFriends);
        log.info("getCommonFriends: {} - Finished", commonFriends);
        return new ArrayList<>(commonFriends);
    }
}
