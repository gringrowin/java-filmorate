package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public Collection<Genre> getAll() {
        String sql = "SELECT * FROM GENRES " +
                    "GROUP BY GENRE_ID";

        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    public Genre getGenre(Integer genreId) {
        checkIdGenre(genreId);

        String sql = "SELECT * FROM GENRES " +
                "WHERE GENRE_ID = ?";

        return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, genreId);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("GENRE_ID"));
        genre.setName(resultSet.getString("GENRE_NAME"));
        return genre;
    }

    private void checkIdGenre(Integer id) {
        String sql = "SELECT * FROM GENRES " +
                "WHERE GENRE_ID = ?" ;
        SqlRowSet rows =  jdbcTemplate.queryForRowSet(sql, id);

        if (!rows.next()) {
            throw new GenreNotFoundException("Genre с ID: " + id + " не найден!");
        }
    }
}
