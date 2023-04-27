package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface FriendStorage {

    User addFriend(User user, User friend);

    User deleteFriend(User user, User friend);

    Set<Integer> getFriends(Integer userId);
}
