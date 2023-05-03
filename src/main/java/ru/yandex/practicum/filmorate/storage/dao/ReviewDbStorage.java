package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review addNewReview(Review review) {
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
        return getReviewById(review.getReviewId()).orElseThrow();
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
    public void delete(int id) {
        String sqlQueryDeleteReview = "DELETE FROM reviews WHERE review_id = ? ";
        jdbcTemplate.update(sqlQueryDeleteReview, id);
    }

    @Override
    public Optional<Review> getReviewById(int id) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM reviews, "  +
                "WHERE review_id = ?", id);
        if (reviewRows.next()) {
            Review review = new Review(
                    reviewRows.getInt("review_id"),
                    reviewRows.getString("content"),
                    reviewRows.getBoolean("is_positive"),
                    reviewRows.getInt("user_id"),
                    reviewRows.getInt("film_id")
            );
            log.info("Найден обзор c id: {} к фильму c id: {}.", id, review.getFilmId());

            return Optional.of(review);
        } else {
            log.info("Обзор с идентификатором {} не найден.", id);
            throw new ReviewNotFoundException("Ревью с id " + id + "  не найден");
        }
    }

    @Override
    public Collection<Review> getReviews() {
        return null;
    }
}
