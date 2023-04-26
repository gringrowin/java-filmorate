package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

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

    @Override
    public Film updateGenreByFilmToStorage(Film film) {

        if (!film.getGenres().isEmpty()) {
            String sqlForDeleteGenre = "DELETE FROM FILMGENRES WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlForDeleteGenre, film.getId());

            for (Genre genre : film.getGenres()) {
                    String sqlForAddGenre = "INSERT INTO FILMGENRES SET FILM_ID = ?, GENRE_ID = ?";
                    jdbcTemplate.update(sqlForAddGenre, film.getId(), genre.getId());
            }
        } else {
            String sqlForDeleteGenre = "DELETE FROM FILMGENRES WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlForDeleteGenre, film.getId());
        }

        return film;
    }

    public Set<Genre> getGenresByFilmFromStorage(Film film) {

        String sql = "SELECT * FROM FILMGENRES, GENRES " +
                "WHERE FILM_ID = ?";

        return new HashSet<>(jdbcTemplate.query(sql, this::mapRowToGenre, film.getId()));
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("GENRE_ID"));
        genre.setName(resultSet.getString("GENRE_NAME"));
        return genre;
    }

    private void checkIdGenre(Integer id) {
        String sql = "SELECT * FROM GENRES " +
                "WHERE GENRE_ID = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);

        if (!rows.next()) {
            throw new GenreNotFoundException("Genre с ID: " + id + " не найден!");
        }
    }
}
