package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Component("dbFilmStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAll() {
        String sql = "SELECT F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, " +
                "F.RELEASE_DATE, F.DURATION, F.RATE, F.MPA_ID " +
                "FROM FILMS AS F " +
                "GROUP BY F.FILM_ID";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public Film add(Film film) {
        String sql = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        film.setId(id);

        return film;
    }

    @Override
    public Film update(Film film) {
        checkIdFilm(film.getId());

        String sql = "UPDATE FILMS SET "
                + "FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATE = ?, MPA_ID = ? "
                + "WHERE FILM_ID = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());

        return film;
    }

    @Override
    public Film getFilm(Integer id) {
        checkIdFilm(id);

            String sql = "SELECT * " +
                    "FROM FILMS AS F, MPA AS M " +
                    "WHERE F.MPA_ID = M.MPA_ID AND F.FILM_ID = ? ";

            return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
    }

    @Override
    public List<Film> getCommonWithFriendFilmsSortedByPopular(Integer userId, Integer friendId) {
        String sql = "SELECT *\n" +
                "FROM FILMS\n" +
                "WHERE FILM_ID IN (\n" +
                "SELECT FILM_ID \n" +
                "FROM LIKES\n" +
                "WHERE USER_ID IN (?,?)\n" +
                "GROUP BY FILM_ID\n" +
                "ORDER BY count(USER_ID) DESC )";

        return jdbcTemplate.query(sql, this::mapRowToFilm, userId, friendId);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
            film.setId(resultSet.getInt("FILM_ID"));
            film.setName(resultSet.getString("FILM_NAME"));
            film.setDescription(resultSet.getString("DESCRIPTION"));
            film.setReleaseDate(Objects.requireNonNull(resultSet.getDate("RELEASE_DATE")).toLocalDate());
            film.setDuration(resultSet.getInt("DURATION"));
            film.setRate(resultSet.getInt("RATE"));

        Mpa mpa = new Mpa();
            mpa.setId(resultSet.getInt("MPA_ID"));
            film.setMpa(mpa);

        return film;
    }

    private void checkIdFilm(Integer id) {
        String sql = "SELECT * FROM FILMS " +
                    "WHERE FILM_ID = ?";
        SqlRowSet rows =  jdbcTemplate.queryForRowSet(sql, id);

        if (!rows.next()) {
            throw new FilmNotFoundException("Фильм с ID: " + id + " не найден!");
        }
    }
}
