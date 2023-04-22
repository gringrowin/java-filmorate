package ru.yandex.practicum.filmorate.storage.dao;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
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
        String sql = "SELECT * FROM USERS;";

        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public User add(User user) {
        setName(user);
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
        setName(user);
        getUser(user.getId());

        String sql = "UPDATE USERS SET " + "EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? "
                + "WHERE  USER_ID = ?";

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
        try {
            String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
            User user = jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
            user.setFriends(getFriends(user.getId()));
            return user;

        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь с идентификатором " + id + " не найден.");
        }
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        getUser(userId);
        getUser(friendId);

        String sql = "INSERT INTO FRIENDS(USER_ID, FRIENDS_ID) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);

        return getUser(userId);
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIENDS_ID = ?";
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
            return user;
    }

    private void setName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private Set<Integer> getFriends(Integer userId) {
        Set<Integer> friends = new HashSet<>();
        try {
            String sql = "SELECT FRIENDS_ID FROM FRIENDS WHERE USER_ID = ?";
            SqlRowSet friendsRows = jdbcTemplate.queryForRowSet(sql, userId);
            while (friendsRows.next()) {
                friends.add(friendsRows.getInt("FRIENDS_ID"));
            }
            return friends;

        } catch (EmptyResultDataAccessException e) {
           return Collections.emptySet();
        }
    }
}
