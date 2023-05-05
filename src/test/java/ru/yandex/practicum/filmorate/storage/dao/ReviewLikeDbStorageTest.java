package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controller.ReviewController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ReviewLikeDbStorageTest {
    @Autowired
    private ReviewController controller;
    ReviewLikeDbStorage reviewLikeDbStorage;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private UserService userService;
    Film testFilm = new Film();;
    User testUser = new User();


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

    }

    @Test
    void addLike() {
        Review testReview = new Review("This film is beautiful.", true,
                testUser.getId(), testFilm.getId());
        controller.addNewReview(testReview);
        reviewLikeDbStorage.addLike(1,1,true);
        assertEquals(1, reviewLikeDbStorage.getLike(1,1),
                "Лайк не добавлен в бд");

    }

    @Test
    void deleteLike() {
    }

    @Test
    void getUsefulness() {
    }
}