package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
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

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Component("dbFilmStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> getAll() {
        String sql = "SELECT F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, " +
                "F.RELEASE_DATE, F.DURATION, F.RATE, F.MPA_ID " +
                "FROM FILMS AS F " +
                "GROUP BY F.FILM_ID";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public Film add(Film film) {
        String sql = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE,MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        film.setId(id);

//        if (!film.getGenres().isEmpty()) {
//            for (Genre genre : film.getGenres()) {
//                String sqlForGenre = "INSERT INTO FILMGENRES SET FILM_ID = ?, GENRE_ID = ?";
//                jdbcTemplate.update(sqlForGenre, film.getId(), genre.getId());
//            }
//        }

        return getFilm(film.getId());
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

//        if (!film.getGenres().isEmpty()) {
//            String sqlForDeleteGenre = "DELETE FROM FILMGENRES WHERE FILM_ID = ?";
//            jdbcTemplate.update(sqlForDeleteGenre, film.getId());
//
//            for (Genre genre : film.getGenres()) {
//                    String sqlForAddGenre = "INSERT INTO FILMGENRES SET FILM_ID = ?, GENRE_ID = ?";
//                    jdbcTemplate.update(sqlForAddGenre, film.getId(), genre.getId());
//            }
//        } else {
//            String sqlForDeleteGenre = "DELETE FROM FILMGENRES WHERE FILM_ID = ?";
//            jdbcTemplate.update(sqlForDeleteGenre, film.getId());
//        }
        return getFilm(film.getId());
    }

    @Override
    public Film getFilm(Integer id) {
        checkIdFilm(id);

            String sql = "SELECT * " +
                    "FROM FILMS AS F, MPA AS M " +
                    "WHERE F.MPA_ID = M.MPA_ID AND F.FILM_ID = ? ";

            return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
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
            mpa.setName(resultSet.getString("MPA_NAME"));
            film.setMpa(mpa);



//        film.setGenres(stringSetGenres(resultSet.getString("GENRE_ID")));

            film.setLikes(resultSet.getInt("LIKES_COUNT"));
        return film;
    }

//    private Set<Genre> stringSetGenres(String genreId) {
//        Set<Genre> genresSet = new HashSet<>();
//
//        if (genreId == null) {
//            return Collections.emptySet();
//        }
//
//        Arrays.stream(genreId.split(","))
//                .map(Integer::parseInt)
//                .forEach(id -> genresSet.add(genreDbStorage.getGenre(id)));
//
//        return genresSet;
//    }

    private void checkIdFilm(Integer id) {
        String sql = "SELECT * FROM FILMS " +
                    "WHERE FILM_ID = ?";
        SqlRowSet rows =  jdbcTemplate.queryForRowSet(sql, id);

        if (!rows.next()) {
            throw new FilmNotFoundException("Фильм с ID: " + id + " не найден!");
        }
    }
}
