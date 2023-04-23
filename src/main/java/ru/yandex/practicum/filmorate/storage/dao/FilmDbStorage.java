package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
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
        checkIdFilm(film.getId());

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

        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                String sqlForGenre = "INSERT INTO FILMGENRES SET FILM_ID = ?, GENRE_ID = ?";
                jdbcTemplate.update(sqlForGenre, film.getId(), genre.getId());
            }
        }
        return getFilm(film.getId());
    }

    @Override
    public Film getFilm(Integer id) {
        checkIdFilm(id);

            String sql = "SELECT F.FILM_ID, F.FILM_TITLE, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, " +
                    "F.MPA_ID, M.MPA_NAME AS MPA_NAME, " +
                    "GROUP_CONCAT(DISTINCT G2.GENRE_ID) AS GENRE_ID, " +
                    "GROUP_CONCAT(DISTINCT G2.GENRE_NAME) AS GENRE_NAME, " +
                    "COUNT(DISTINCT L.USER_ID) AS LIKES " +
                    "FROM FILMS AS F " +
                    "LEFT JOIN MPA M on M.MPA_ID = F.MPA_ID " +
                    "LEFT JOIN LIKES L on L.FILM_ID = f.FILM_ID " +
                    "LEFT JOIN FILMGENRES F2 on F2.FILM_ID = f.FILM_ID " +
                    "LEFT JOIN GENRES G2 on G2.GENRE_ID = F2.GENRE_ID " +
                    "WHERE F.FILM_ID = ? " +
                    "GROUP BY F.FILM_ID";

            return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        checkIdFilm(filmId);
        String sql = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);

        return getFilm(filmId);
    }

    @Override
    public Film deleteLike(Integer filmId, Integer userId) {
        checkIdFilm(filmId);
        String sql = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);

        return getFilm(filmId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sql = "SELECT F.FILM_ID, F.FILM_TITLE, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, " +
                "F.MPA_ID, M.MPA_NAME, " +
                "GROUP_CONCAT(DISTINCT G2.GENRE_ID) AS GENRE_ID, " +
                "GROUP_CONCAT(DISTINCT G2.GENRE_NAME) AS GENRE_NAME, " +
                "COUNT(DISTINCT L.USER_ID) AS LIKES_COUNT " +
                "FROM FILMS AS F " +
                "LEFT JOIN MPA M on M.MPA_ID = F.MPA_ID " +
                "LEFT JOIN LIKES L on L.FILM_ID = f.FILM_ID " +
                "LEFT JOIN FILMGENRES F2 on F2.FILM_ID = f.FILM_ID " +
                "LEFT JOIN GENRES G2 on G2.GENRE_ID = F2.GENRE_ID " +
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
            film.setRate(resultSet.getInt("LIKES_COUNT"));
        Mpa mpa = new Mpa();
            mpa.setId(resultSet.getInt("MPA_ID"));
            mpa.setName(resultSet.getString("MPA_NAME"));
            film.setMpa(mpa);


                Set<Genre> genreSet = new HashSet<>();
                for (int i = 0; i < resultSet.getArray("GENRE_ID").getResultSet().getFetchSize(); i++) {
                    genreSet.add(mapRowToGenre(resultSet.getArray("GENRE_ID").getResultSet(), i));
                }
                film.setGenres(genreSet);

            return film;
    }

    private Genre mapRowToGenre (ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("GENRE_ID"));
        genre.setName(resultSet.getString("GENRE_NAME"));
        return genre;
    }

    private void checkIdFilm(Integer id) {
        String sql = "SELECT * FROM FILMS " +
                    "WHERE FILM_ID = ?" ;
        SqlRowSet rows =  jdbcTemplate.queryForRowSet(sql, id);

        if (!rows.next()) {
            throw new FilmNotFoundException("Фильм с ID: " + id + " не найден!");
        }
    }
}
