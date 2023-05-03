package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public Film addLike(Film film, Integer userId) {
        checkIdFilm(film.getId());
        checkIdUser(userId);

        String sql = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, film.getId(), userId);
        film.setLikes(getLikes(film.getId()));

        return film;
    }

    public Film deleteLike(Film film, Integer userId) {
        checkIdFilm(film.getId());
        checkIdUser(userId);

        String sql = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, film.getId(), userId);

        film.setLikes(getLikes(film.getId()));
        return film;
    }

    @Override
    public Integer getLikes(Integer filmId) {
        checkIdFilm(filmId);

        String sql = "SELECT COUNT(DISTINCT USER_ID) AS COUNT FROM LIKES " +
                "WHERE FILM_ID = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt("COUNT"), filmId);
    }
    @Override
    public List<Integer> getLikedFilmsByUserId(int userId){
        String sql = "SELECT film_id FROM Likes WHERE user_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId);
        List<Integer> filmIds = new ArrayList<>();
        while (sqlRowSet.next()){
            filmIds.add(sqlRowSet.getInt("film_id"));
        }
        return filmIds;
    }

    private void checkIdFilm(Integer id) {
        String sql = "SELECT * FROM FILMS " +
                "WHERE FILM_ID = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);

        if (!rows.next()) {
            throw new FilmNotFoundException("Фильм с ID: " + id + " не найден!");
        }
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
