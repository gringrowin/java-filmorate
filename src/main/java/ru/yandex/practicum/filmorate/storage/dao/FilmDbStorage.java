package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("dbFilmStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> getAll() {
        String sql = "SELECT * FROM FILMS " +
                    "JOIN MPA M on M.MPA_ID = FILMS.MPA_ID";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public Film add(Film film) {
        String sql = "INSERT INTO FILMS (FILM_TITLE, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
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
       // getFilm(film.getId());
        String sql = "UPDATE FILMS SET "
                + "FILM_TITLE = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? "
                + "WHERE FILM_ID = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public Film getFilm(Integer id) {
        try {
            String sql = "SELECT F.FILM_ID, F.FILM_TITLE, F.DESCRIPTION, F.RELEASE_DATE, " +
                    "F.MPA_ID, M.MPA_NAME, " +
                    "GROUP_CONCAT(DISTINCT G2.GENRE_ID) AS GENRE_ID, " +
                    "GROUP_CONCAT(DISTINCT G2.GENRE_NAME) AS GENRE_NAME, " +
                    "COUNT(DISTINCT L.USER_ID) AS LIKES " +
                    "FROM FILMS AS F " +
                    "JOIN MPA M on M.MPA_ID = F.MPA_ID " +
                    "JOIN LIKES L on L.FILM_ID = f.FILM_ID " +
                    "JOIN FILMGENRES F2 on F2.FILM_ID = f.FILM_ID " +
                    "JOIN GENRES G2 on G2.GENRE_ID = F2.GENRE_ID " +
                    "WHERE F.FILM_ID = ? " +
                    "GROUP BY F.FILM_ID";

            return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);

        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Фильм с идентификатором " + id + " не найден.");
        }
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);

        return getFilm(filmId);
    }

    @Override
    public Film deleteLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);

        return getFilm(filmId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sql = "SELECT F.FILM_ID, F.FILM_TITLE, F.DESCRIPTION, F.RELEASE_DATE, " +
                "F.MPA_ID, M.MPA_NAME, " +
                "GROUP_CONCAT(DISTINCT G2.GENRE_ID) AS GENRE_ID, " +
                "GROUP_CONCAT(DISTINCT G2.GENRE_NAME) AS GENRE_NAME, " +
                "COUNT(DISTINCT L.USER_ID) AS LIKES_COUNT " +
                "FROM FILMS AS F " +
                "JOIN MPA M on M.MPA_ID = F.MPA_ID " +
                "JOIN LIKES L on L.FILM_ID = f.FILM_ID " +
                "JOIN FILMGENRES F2 on F2.FILM_ID = f.FILM_ID " +
                "JOIN GENRES G2 on G2.GENRE_ID = F2.GENRE_ID " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY LIKES_COUNT DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, this::mapRowToFilm, count);

    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
            film.setId(resultSet.getInt("FILM_ID"));
            film.setName(resultSet.getString("FILM_TITLE"));
            film.setDescription(resultSet.getString("DESCRIPTION"));
            film.setReleaseDate(Objects.requireNonNull(resultSet.getDate("RELEASE_DATE")).toLocalDate());
            film.setDuration(resultSet.getInt("DURATION"));
        Mpa mpa = new Mpa();
            mpa.setId(resultSet.getInt("MPA_ID"));
            mpa.setName(resultSet.getString("MPA_NAME"));
            film.setMpa(mpa);


            Set<Genre> genreSet = new HashSet<>();
            for (int i = 0; i < resultSet.getArray("GENRE_ID").getResultSet().getFetchSize(); i++) {
                genreSet.add(mapRowToGenres(resultSet.getArray("GENRE_ID").getResultSet(), 1));
            }
            film.setGenres(genreSet);

            return film;
    }

    private Genre mapRowToGenres (ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("GENRE_ID"));
        genre.setName(resultSet.getString("GENRE_NAME"));
        return genre;
    }
}
