package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component("dbDirectorStorage")
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Director create(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Directors")
                .usingGeneratedKeyColumns("director_id");

        Map<String, Object> directorTable = new HashMap<>();
        directorTable.put("director_name", director.getName());

        int directorId = simpleJdbcInsert.executeAndReturnKey(directorTable).intValue();

        return getById(directorId);
    }

    @Override
    public Director update(Director director) {
        String sql = "UPDATE Directors " +
                "SET director_name = ? " +
                "WHERE director_id = ?";

        jdbcTemplate.update(sql, director.getName(), director.getId());

        return getById(director.getId());
    }

    @Override
    public Director getById(Integer directorId) {
        String sql = "SELECT * " +
                "FROM Directors " +
                "WHERE director_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::directorMapping, directorId);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public Set<Director> getByFilmId(Integer filmId) {
        String sql = "SELECT fd.director_id, d.director_name " +
                "FROM Film_directors AS fd " +
                "INNER JOIN Directors AS d ON fd.director_id = d.director_id " +
                "WHERE film_id = ?";
        try {
            return new HashSet<>(jdbcTemplate.query(sql, this::directorMapping, filmId));
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public void updateDirectorsByFilmToStorage(Film film) {
        String sqlForDeleteDirectors = "DELETE FROM FILM_DIRECTORS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlForDeleteDirectors, film.getId());
        if (!film.getDirectors().isEmpty()) {
            for (Director director : film.getDirectors()) {
                String sqlForAddDirectorsToFilm = "INSERT INTO FILM_DIRECTORS SET FILM_ID = ?, DIRECTOR_ID = ?";
                jdbcTemplate.update(sqlForAddDirectorsToFilm, film.getId(), director.getId());
            }
        }
    }

    @Override
    public Set<Director> getDirectorsByFilmFromStorage(Integer filmId) {

        String sql = "SELECT FD.DIRECTOR_ID, D.DIRECTOR_NAME FROM FILM_DIRECTORS AS FD " +
                "LEFT JOIN DIRECTORS AS D on D.DIRECTOR_ID = FD.DIRECTOR_ID " +
                "WHERE FILM_ID = ?";

        return new HashSet<>(jdbcTemplate.query(sql, this::directorMapping, filmId));
    }

    @Override
    public Set<Director> getAll() {
        String sql = "SELECT * " +
                "FROM Directors " +
                "ORDER BY director_id";
        return new HashSet<>(jdbcTemplate.query(sql, this::directorMapping));
    }

    @Override
    public Director delete(Integer directorId) {
        String sql = "DELETE FROM Directors " +
                "WHERE director_id = ?";
        Director director = getById(directorId);
        jdbcTemplate.update(sql, directorId);
        return director;
    }

    private Director directorMapping(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Director(resultSet.getInt("director_id"),
                resultSet.getString("director_name"));
    }
}
