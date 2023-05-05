package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ReviewControllerTest {
    private ReviewController controller;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private UserService userService;
    Film testFilm = new Film();;
    User testUser = new User();
    User testUser2 = new User();

    @BeforeEach
    public void beforeEach() {
        controller = new ReviewController(reviewService);
        testFilm.setId(1);
        testFilm.setDuration(120);
        testFilm.setReleaseDate(LocalDate.of(2015, 1, 1));
        testFilm.setName("Friends");
        testFilm.setDescription("film");
        Mpa mpa = new Mpa();
        mpa.setId(1);
        testFilm.setMpa(mpa);
        filmService.create(testFilm);

        testUser.setId(1);
        testUser.setName("name");
        testUser.setBirthday(LocalDate.of(2000, 1, 1));
        testUser.setLogin("login");
        testUser.setEmail("mail@mail.ru");
        userService.create(testUser);

        testUser2.setId(2);
        testUser2.setName("name2");
        testUser2.setBirthday(LocalDate.of(2000, 2, 2));
        testUser2.setLogin("login2");
        testUser2.setEmail("mail2@mail.ru");
        userService.create(testUser2);
    }

    @Test
    void addNewReview() {
        Review testReview = new Review("This film is beautiful.", true,
                testUser.getId(), testFilm.getId());
        Review testReviewFromBd = controller.addNewReview(testReview);
        assertEquals(testReview, testReviewFromBd, "Обзор не внесён в базу данных");
    }

    @Test
    void addNewReviewWithNullId() {
        Review testReview = new Review("This film is beautiful.", true,
                testUser.getId(), null);
        Review testReview2 = new Review("This film is beautiful.", true,
                null, testFilm.getId());
        assertThrows(NullPointerException.class, () -> controller.addNewReview(testReview));
        assertThrows(NullPointerException.class, () -> controller.addNewReview(testReview2));
    }

    @Test
    void updateReview() {
        Review testReview = new Review("This film is beautiful.", true,
                testUser.getId(), testFilm.getId());
        Review updateTestReview = new Review("Update. This film is negative.", false,
                testUser.getId(), testFilm.getId());
        controller.addNewReview(testReview);
        updateTestReview.setReviewId(testReview.getReviewId());
        Review testReviewFromBd = controller.updateReview(updateTestReview);
        assertEquals(updateTestReview, testReviewFromBd, "Отзыв не внесён в базу данных");
    }

    @Test
    void deleteReview() {
        Review testReview = new Review("This film is beautiful.", true,
                testUser.getId(), testFilm.getId());
        controller.addNewReview(testReview);
        controller.deleteReview(testReview.getReviewId());
        assertThrows(ReviewNotFoundException.class, () -> controller.getReviewById(testReview.getReviewId()));

    }

    @Test
    void getReviewById() {
        Review testReview = new Review("This film is beautiful.", true,
                testUser.getId(), testFilm.getId());
        Review testReview2 = new Review("This film is ugly.", false,
                testUser2.getId(), testFilm.getId());
        controller.addNewReview(testReview);
        Review review2FromBd = controller.addNewReview(testReview2);
        testReview2.setReviewId(review2FromBd.getReviewId());
        assertEquals(testReview2, controller.getReviewById(testReview2.getReviewId()),
                "Отзыв не вернулся");
    }

    @Test
    void getReviewsToFilm() {
        Review testReview = new Review("This film is beautiful.", true,
                testUser.getId(), testFilm.getId());
        Review testReview2 = new Review("This film is ugly.", false,
                testUser2.getId(), testFilm.getId());
        Review review1FromBd = controller.addNewReview(testReview);
        Review review2FromBd = controller.addNewReview(testReview2);

        Collection<Review> testCollectionReviews = new ArrayList<>();
        testCollectionReviews.add(review1FromBd);
        testCollectionReviews.add(review2FromBd);

        assertEquals(testCollectionReviews, controller.getReviewsToFilm(testFilm.getId(), null),
                "коллекция отзывов не сформирована, или не вернулась");
    }

    @Test
    void addLikeToReview() {
        Review testReview = new Review("This film is beautiful.", true,
                testUser.getId(), testFilm.getId());
        controller.addNewReview(testReview);
        controller.addLikeToReview(testFilm.getId(),testUser.getId());
        assertEquals(1, controller.getReviewById(testReview.getReviewId()).getUseful(),
                "Лайк не добавлен, либо некорректно сформирована оценка полезности ");
    }

    @Test
    void addDisLikeToReview() {
        Review testReview = new Review("This film is beautiful.", true,
                testUser.getId(), testFilm.getId());
        controller.addNewReview(testReview);
        controller.addDisLikeToReview(testFilm.getId(),testUser.getId());
        assertEquals(-1, controller.getReviewById(testReview.getReviewId()).getUseful(),
                "ДизЛайк не добавлен, либо некорректно сформирована оценка полезности ");
    }

    @Test
    void deleteLikeFromReview() {
    }

    @Test
    void deleteDisLikeFromReview() {
    }
}