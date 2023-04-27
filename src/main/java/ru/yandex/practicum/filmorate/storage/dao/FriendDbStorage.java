package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addFriend(User user, User friend) {

        String sql = "INSERT INTO FRIENDS(USER_ID, FRIEND_ID) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getId(), friend.getId());

        return user;
    }

    @Override
    public User deleteFriend(User user, User friend) {
        String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, user.getId(), friend.getId());

        return user;
    }

    @Override
    public Set<Integer> getFriends(Integer userId) {
        Set<Integer> friends = new HashSet<>();
        try {
            String sql = "SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?";
            SqlRowSet friendsRows = jdbcTemplate.queryForRowSet(sql, userId);
            while (friendsRows.next()) {
                friends.add(friendsRows.getInt("FRIEND_ID"));
            }
            return friends;

        } catch (EmptyResultDataAccessException e) {
            return Collections.emptySet();
        }
    }
}
