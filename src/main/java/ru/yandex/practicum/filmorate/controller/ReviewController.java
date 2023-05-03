package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping()
    public Review addNewReview(@RequestBody Review review){
        return reviewService.addNewReview(review);
    }




}

/*`PUT /reviews`

Редактирование уже имеющегося отзыва.

`DELETE /reviews/{id}`

Удаление уже имеющегося отзыва.

`GET /reviews/{id}`

Получение отзыва по идентификатору.

`GET /reviews?filmId={filmId}&count={count}`
Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано то 10.

- `PUT /reviews/{id}/like/{userId}`  — пользователь ставит лайк отзыву.
- `PUT /reviews/{id}/dislike/{userId}`  — пользователь ставит дизлайк отзыву.
- `DELETE /reviews/{id}/like/{userId}`  — пользователь удаляет лайк/дизлайк отзыву.
- `DELETE /reviews/{id}/dislike/{userId}`  — пользователь удаляет дизлайк отзыву.
 */
