package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.OperationType;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Component("dbFeedStorage")
@RequiredArgsConstructor
public class FeedDbStorage implements FeedStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Feed> getFeed(int userId) {
        checkIdUser(userId);
        String sql = "SELECT * " +
                "FROM Feed AS f " +
                "WHERE f.user_id = ? " +
                "ORDER BY f.event_id";

        return jdbcTemplate.query(sql, this::mapRowToFeed, userId);
    }

    @Override
    public void addFeedEvent(EventType eventType, OperationType operationType, int userId, int entityId) {
        String sql = "INSERT INTO Feed " +
                "SET " +
                "event_timestamp = ?, " +
                "user_id = ?, " +
                "event_type = ?, " +
                "operation = ?, " +
                "entity_id = ?";

        jdbcTemplate.update(sql,
                Instant.now().toEpochMilli(),
                userId,
                eventType.toString(),
                operationType.toString(),
                entityId);
    }


    private Feed mapRowToFeed(ResultSet rs, int rowNum) throws SQLException {
        return Feed.builder()
                .timestamp(rs.getLong("event_timestamp"))
                .eventType(EventType.valueOf(rs.getString("event_type")))
                .operation(OperationType.valueOf(rs.getString("operation")))
                .userId(rs.getLong("user_id"))
                .eventId(rs.getLong("event_id"))
                .entityId(rs.getLong("entity_id"))
                .build();
    }

    private void checkIdUser(Integer id) {
        String sql = "SELECT * FROM USERS " +
                "WHERE USER_ID = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);

        if (!rows.next()) {
            throw new UserNotFoundException("Пользователь с ID: " + id + " не найден!");
        }
    }
}
