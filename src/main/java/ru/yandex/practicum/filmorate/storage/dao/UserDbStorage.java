package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("dbUserStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> getAll() {
        String sql = "SELECT * FROM USERS " +
                "GROUP BY USER_ID";

        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public User add(User user) {
        String sql = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) " +
                "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        user.setId(id);

        return user;
    }

    @Override
    public User update(User user) {
        checkIdUser(user.getId());
        String sql = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? "
                + "WHERE USER_ID = ?";

        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());

        return user;
    }

    @Override
    public User getUser(Integer id) {
        checkIdUser(id);
        String sql = "SELECT U.USER_ID, U.EMAIL, U.LOGIN, U.USER_NAME, U.BIRTHDAY " +
                "FROM USERS AS U " +
                "WHERE U.USER_ID = ? " +
                "GROUP BY U.USER_ID";

        return jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        checkIdUser(userId);
        checkIdUser(friendId);

        String sql = "INSERT INTO FRIENDS(USER_ID, FRIEND_ID) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);

        return getUser(userId);
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);

        return getUser(userId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
            user.setId(resultSet.getInt("USER_ID"));
            user.setEmail(resultSet.getString("EMAIL"));
            user.setLogin(resultSet.getString("LOGIN"));
            user.setName(resultSet.getString("USER_NAME"));
            user.setBirthday(Objects.requireNonNull(resultSet.getDate("BIRTHDAY")).toLocalDate());
            user.setFriends(getFriends(user.getId()));
            return user;
    }

    private void checkIdUser(Integer id) {
        try {
            String sql = "SELECT * FROM USERS " +
                    "WHERE USER_ID = ?";
            jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь с ID: " + id + " не найден!");
        }
    }

    private Set<Integer> getFriends(Integer userId) {
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
