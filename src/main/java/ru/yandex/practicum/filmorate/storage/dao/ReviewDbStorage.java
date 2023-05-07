package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository("dbReviewStorage")
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review addNewReview(Review review) {
        idValidation(review);
        String sqlQueryReview = "insert into reviews(content, is_positive, user_id, film_id) " +
                "values (?, ?, ?, ?) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();           // вернуть id, сгенерированный в БД
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQueryReview, new String[]{"review_id"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setInt(3, review.getUserId());
            stmt.setInt(4, review.getFilmId());
            return stmt;
        }, keyHolder);
        review.setReviewId(keyHolder.getKey().intValue());

        log.info("отзыву к фильму {}, добавленному пользователем {}, присвоен id {}",
                review.getFilmId(), review.getUserId(), review.getReviewId());
        return getReviewById(review.getReviewId()).get();
    }

    @Override
    public Review update(Review review) {

        String sqlQuery = "UPDATE reviews SET " +
                "content = ?, is_positive = ? " +
                "WHERE review_id = ? ";
        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());
        return getReviewById(review.getReviewId()).orElseThrow();
    }

    @Override
    public void delete(Integer id) {
        String sqlQueryDeleteReview = "DELETE FROM reviews WHERE review_id = ? ";
        jdbcTemplate.update(sqlQueryDeleteReview, id);
        log.info("Отзыв с идентификатором " + id + " удалён из базы");
    }

    @Override
    public Optional<Review> getReviewById(Integer id) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM reviews " +
                "WHERE review_id = ?", id);
        if (reviewRows.next()) {
            Review review = new Review(
                    reviewRows.getInt("review_id"),
                    reviewRows.getString("content"),
                    reviewRows.getBoolean("is_positive"),
                    reviewRows.getInt("user_id"),
                    reviewRows.getInt("film_id")
            );
            log.info("Найден отзыв c id: {} к фильму c id: {}.", id, review.getFilmId());

            return Optional.of(review);
        } else {
            log.error("Отзыв с идентификатором {} не найден.", id);
            throw new ReviewNotFoundException("Ревью с id " + id + "  не найден");
        }
    }

    public Collection<Review> getReviews(Integer filmId, Integer countOfReviews) {
        String sqlWhereFilmId = (filmId != null) ? " WHERE film_id= " + filmId + " " : "";

        String sqlQuery = "SELECT *, " +
                "(COUNT(SELECT user_id FROM reviews_likes AS rl WHERE rl.review_id = r.review_id AND is_like = TRUE) - " +
                "COUNT(SELECT user_id FROM reviews_likes AS rl WHERE rl.review_id = r.review_id AND is_like = FALSE)) as useful " +
                "FROM reviews AS r " + sqlWhereFilmId +
                "GROUP BY review_id " +
                "ORDER BY useful DESC " +
                "LIMIT " + countOfReviews;
        Collection<Review> allReviews = new ArrayList<>();
        allReviews = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeReviews(rs));
        return allReviews;
    }

    private Review makeReviews(ResultSet rs) throws SQLException {
        String sql = "SELECT * " +
                "FROM films AS f JOIN rate_mpa AS rm ON f.rate_id = rm.rate_id ";
        return new Review(
                rs.getInt("review_id"),
                rs.getString("content"),
                rs.getBoolean("is_positive"),
                rs.getInt("user_id"),
                rs.getInt("film_id"),
                rs.getInt("useful"));
    }

    private void idValidation(Review review) {
        if (review.getUserId() < 1) {
            throw new UserNotFoundException("id пользователя не может быть меньше 1");
        }
        if (review.getFilmId() < 1) {
            throw new FilmNotFoundException("id фильма не может быть меньше 1");
        }
    }
}