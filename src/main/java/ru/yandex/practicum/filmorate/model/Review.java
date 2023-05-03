package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Review {
    private final Integer reviewId;
    @NotBlank
    private String content;
    private Boolean isPositive;
    private final Integer userId;
    private final Integer filmId;
    private Integer useful = 0;         //рейтинг полезности

    public Review(int reviewId, String content, boolean isPositive, int userId, int filmId) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }

    public void setReviewId(int intValue) {
    }
}

/*
В приложении должны появиться отзывы на фильмы. Добавленные отзывы должны иметь рейтинг и несколько
дополнительных характеристик.
1. Оценка — полезно/бесполезно.
2. Тип отзыва — негативный/положительный.
Рейтинг отзыва.
У отзыва имеется рейтинг. При создании отзыва рейтинг равен нулю. Если пользователь оценил отзыв как полезный,
это увеличивает его рейтинг на 1. Если как бесполезный, то уменьшает на 1.
Отзывы должны сортироваться по рейтингу полезности.
 */
