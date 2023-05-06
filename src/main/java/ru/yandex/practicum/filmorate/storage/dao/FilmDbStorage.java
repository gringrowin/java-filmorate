package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.FilmSortBy;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
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
    public List<Film> getPopularFilms(Integer count, Integer genreId, Integer year) {
        List<Film> popularFilms;
        String yearFilter = "WHERE YEAR(f.RELEASE_DATE) = ? ";
        String genreFilter = "WHERE fg.GENRE_ID = ? ";
        String genreJoin = "JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID ";
        String genreAndYearFilter = "WHERE fg.GENRE_ID = ? AND YEAR(f.RELEASE_DATE) = ? ";
        StringBuilder sql = new StringBuilder("SELECT F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, " +
                "F.RELEASE_DATE, F.DURATION, F.RATE, F.MPA_ID " +
                "FROM FILMS AS F " +
                "LEFT OUTER JOIN LIKES AS l ON l.film_id = f.film_id ");
        String sqlEnd = "GROUP BY F.FILM_ID ORDER BY COUNT(l.user_id) DESC LIMIT (?)";

        if (genreId == null && year == null) {
            String sqlString = sql.append(sqlEnd).toString();
            popularFilms = jdbcTemplate.query(sqlString, this::mapRowToFilm, count);

        } else if (genreId == null && year != null) {
            String sqlString = sql.append(yearFilter).append(sqlEnd).toString();
            popularFilms = jdbcTemplate.query(sqlString, this::mapRowToFilm, year, count);

        } else if (genreId != null && year == null) {
            String sqlString = sql.append(genreJoin).append(genreFilter).append(sqlEnd).toString();
            popularFilms = jdbcTemplate.query(sqlString, this::mapRowToFilm, genreId, count);

        } else {
            String sqlString = sql.append(genreJoin).append(genreAndYearFilter).append(sqlEnd).toString();
            popularFilms = jdbcTemplate.query(sqlString, this::mapRowToFilm, genreId, year, count);
        }
        return popularFilms;
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
    public List<Film> searchFilms(String query, String[] paramsForFinding) {
        String queryByOneParam = paramsForFinding[0].equals("title") ?
                " LOWER(F.FILM_NAME) LIKE LOWER('%" + query + "%')" :
                " LOWER(D.DIRECTOR_NAME) LIKE LOWER('%" + query + "%')";
        String queryByTwoParams = paramsForFinding.length == 2 ?
                "OR LOWER(DIRECTOR_NAME) LIKE LOWER('%" + query + "%')" : "";

        String sql = "SELECT F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, " +
                "F.RELEASE_DATE, F.DURATION, F.RATE, F.MPA_ID, " +
                "COUNT(DISTINCT L.USER_ID) AS COUNT_LIKES, FD.DIRECTOR_ID, D.DIRECTOR_NAME " +
                "FROM FILMS AS F " +
                "LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID " +
                "LEFT JOIN FILM_DIRECTORS AS FD ON F.FILM_ID = FD.FILM_ID " +
                "LEFT JOIN DIRECTORS D on D.DIRECTOR_ID = FD.DIRECTOR_ID " +
                "WHERE " + queryByOneParam + queryByTwoParams + " " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY COUNT_LIKES DESC";

        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public List<Film> getFilmsByDirectorIdAndSort(Integer directorId, FilmSortBy sortBy) {
        StringBuilder sql = new StringBuilder(
                "SELECT *, COUNT(*) AS likes " +
                        "FROM Films AS f " +
                        "INNER JOIN Film_directors AS fd ON f.film_id = fd.film_id " +
                        "INNER JOIN Mpa AS m ON f.mpa_id = m.mpa_id " +
                        "LEFT JOIN Likes AS l ON f.film_id = l.film_id " +
                        "WHERE fd.director_id = ? " +
                        "GROUP BY f.film_id ");

        if (sortBy.equals(FilmSortBy.likes)) {
            sql.append("ORDER BY likes");
        }
        if (sortBy.equals(FilmSortBy.year)) {
            sql.append("ORDER BY f.release_date");
        }

        return jdbcTemplate.query(sql.toString(), this::mapRowToFilm, directorId);
    }


    @Override
    public List<Film> getCommonFilmsForFriendSortedByPopular(Integer userId, Integer friendId) {
        String sql = "SELECT * FROM FILMS WHERE FILM_ID in " +
                "(SELECT FILM_ID FROM LIKES WHERE USER_ID IN (?,?) " +
                "GROUP BY FILM_ID HAVING COUNT(DISTINCT USER_ID) = 2 ORDER BY count(USER_ID) DESC )";

        return jdbcTemplate.query(sql, this::mapRowToFilm, userId, friendId);
    }

    @Override
    public void deleteFilm(Integer filmId) {
        checkIdFilm(filmId);

        String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";

        jdbcTemplate.update(sql, filmId);
        log.info("deleteFilm: {} - Finished", filmId);
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
