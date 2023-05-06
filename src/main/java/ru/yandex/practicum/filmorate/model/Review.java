package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private int reviewId;
    @NotBlank(message = "Текст отзыва не может быть пустым")
    private String content;
    @NotNull(message = "Категория отзыва не может быть пустой")
    private Boolean isPositive;
    @NotNull(message = "Не получен id пользователя")
    private Integer userId;
    @NotNull(message = "Не получен id фильма")
    private Integer filmId;
    private Integer useful = 0;         //рейтинг полезности

    public Review(int reviewId, String content, boolean isPositive, int userId, int filmId) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }

    public Review(String content, boolean isPositive, Integer userId, Integer filmId) {
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }
}