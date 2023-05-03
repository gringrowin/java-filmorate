package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;
import java.util.Optional;

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

    @PutMapping()
    public Review updateReview(@RequestBody Review review){
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable int id){
        reviewService.deleteReview(id);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable int id){
        return reviewService.getReviewById(id);
    }

    @GetMapping("/{filmId}/{count}")
    public Collection<Review> getReviewsByFilmId(@RequestParam Optional<Integer> filmId,
                                                 @RequestParam Optional<Integer> count) {
        return filmId.isPresent() ? reviewService.getReviews(filmId.get(), count.orElse(10)) :
                reviewService.getAllReviews(count.orElse(10));
    }
/*
`GET /reviews?filmId={filmId}&count={count}`
Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано то 10.
*/
}


/*
- `PUT /reviews/{id}/like/{userId}`  — пользователь ставит лайк отзыву.
- `PUT /reviews/{id}/dislike/{userId}`  — пользователь ставит дизлайк отзыву.
- `DELETE /reviews/{id}/like/{userId}`  — пользователь удаляет лайк/дизлайк отзыву.
- `DELETE /reviews/{id}/dislike/{userId}`  — пользователь удаляет дизлайк отзыву.
 */
