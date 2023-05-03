package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Review {
    private Integer reviewId;
    @NotBlank
    private String content;
    private Boolean isPositive;
    private Integer userId;
    private Integer filmId;
    private Integer useful = 0;         //рейтинг полезности
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
