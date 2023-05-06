package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.ReviewLikeStorage;

@Slf4j
@Repository("dbReviewLikeStorage")
public class ReviewLikeDbStorage implements ReviewLikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewLikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(int reviewId, int userId, boolean isLike) {     // добавляет и лайки и дизлайки
        String sqlQueryLike = "INSERT INTO reviews_likes (review_id, user_id, is_like) " +
                "VALUES(?, ?, ?) ";

        jdbcTemplate.update(sqlQueryLike,
                reviewId,
                userId,
                isLike);
    }

    public Integer getLike(int reviewId, int userId) {
        String sqlQueryForLike = "SELECT COUNT(review_id) AS cntLikes FROM reviews_likes WHERE is_like = true " +
                "AND review_id = ? AND user_id = ? ";
        SqlRowSet reviewLikeRows = jdbcTemplate.queryForRowSet(sqlQueryForLike, reviewId, userId);

        Integer likesCount = 0;
        if (reviewLikeRows.next()) {
            likesCount = (reviewLikeRows.getInt("cntLikes"));
        }
        return likesCount;
    }

    @Override
    public void deleteLike(int reviewId, int userId) {
        String sqlQueryDeleteLike = "DELETE FROM reviews_likes WHERE review_id = ?" +
                " AND user_id = ? ";
        jdbcTemplate.update(sqlQueryDeleteLike,
                reviewId,
                userId);
    }

    @Override
    public Integer getUsefulness(int id) {
        String sqlQueryForLikes = "SELECT COUNT(review_id) AS cntLikes FROM reviews_likes WHERE is_like = true " +
                "AND review_id = ? ";
        String sqlQueryForDisLikes = "SELECT COUNT(review_id) AS cntDisLikes FROM reviews_likes " +
                "WHERE is_like = false " +
                "AND review_id = ? ";

        SqlRowSet reviewLikesRows = jdbcTemplate.queryForRowSet(sqlQueryForLikes, id);
        SqlRowSet reviewDisLikesRows = jdbcTemplate.queryForRowSet(sqlQueryForDisLikes, id);

        int likesCount = 0;
        int disLikesCount = 0;
        if (reviewLikesRows.next()) {
            likesCount = (reviewLikesRows.getInt("cntLikes"));
            log.info("Обзор id: {} считают полезным {} пользователей", id, likesCount);
        }
        if (reviewDisLikesRows.next()) {
            disLikesCount = (reviewDisLikesRows.getInt("cntDisLikes"));
            log.info("Обзор id: {} считают бесполезным {} пользователей", id, disLikesCount);
        }
        if (likesCount - disLikesCount == 0) {
            log.info("Обзор id: {} не получил оценку полезности со стороны пользователей", id);
        }
        return likesCount - disLikesCount;

    }
}
