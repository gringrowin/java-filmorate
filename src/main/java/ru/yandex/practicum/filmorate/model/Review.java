package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Review {
    private Integer reviewId;
    @NotNull(message = "Текст отзыва не может быть пустым")
    private String content;
    @NotNull(message = "Категория отзыва не может быть пустой")
    private Boolean isPositive;
    @NotNull(message = "Не получен id пользователя")
    private final Integer userId;
    @NotNull(message = "Не получен id фильма")
    private final Integer filmId;
    private Integer useful = 0;         //рейтинг полезности

    public Review(int reviewId, String content, boolean isPositive, int userId, int filmId) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }

    public Review(String content, Boolean isPositive, Integer userId, Integer filmId) {
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
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
