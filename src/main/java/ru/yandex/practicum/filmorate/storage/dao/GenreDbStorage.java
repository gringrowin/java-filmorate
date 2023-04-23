package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
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

        return jdbcTemplate.query(sql, this::mapRowToGenres);
    }

    public Genre getGenre(Integer genreId) {
        String sql = "SELECT * FROM GENRES " +
                "WHERE GENRE_ID = ?";

        return jdbcTemplate.queryForObject(sql, this::mapRowToGenres, genreId);
    }

    private Genre mapRowToGenres (ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("GENRE_ID"));
        genre.setName(resultSet.getString("GENRE_NAME"));
        return genre;
    }
}
