package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public List<Mpa> getAll() {
        String sql = "SELECT * FROM MPA " +
                "ORDER BY MPA_ID";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    public Mpa getMpa(Integer mpaId) {
        checkIdMpa(mpaId);
        String sql = "SELECT * FROM MPA " +
                "WHERE MPA_ID = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, mpaId);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("MPA_ID"));
        mpa.setName(resultSet.getString("MPA_NAME"));
        return mpa;
    }

    private void checkIdMpa(Integer id) {
        String sql = "SELECT * FROM MPA " +
                "WHERE MPA_ID = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);

        if (!rows.next()) {
            throw new MpaNotFoundException("MPA с ID: " + id + " не найден!");
        }
    }
}
