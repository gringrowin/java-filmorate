package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PopularFilmsTest {
    private final FilmService filmService;
    private final UserService userService;
    private final LikeService likeService;
    Film film1;
    Film film2;
    Film film3;
    Film film4;
    Film film5;
    User user1;

    void filmInit() {
        Genre comedy = new Genre();
        comedy.setName("Комедия");
        comedy.setId(1);
        Genre drama = new Genre();
        drama.setName("Драма");
        drama.setId(2);
        Genre cartoon = new Genre();
        cartoon.setName("Мультфильм");
        cartoon.setId(3);

        Mpa g = new Mpa();
        g.setId(1);
        g.setName("G");

        film1 = new Film();
        film1.setId(1);
        film1.setName("Матрица");
        film1.setDescription("Описание");
        film1.setDuration(120);
        film1.setReleaseDate(LocalDate.of(2000, 10, 10));
        film1.setGenres(Set.of(comedy));
        film1.setMpa(g);
        film1.setRate(0);
        film1.setLikes(1);

        film2 = new Film();
        film2.setId(2);
        film2.setName("Матрица2");
        film2.setDescription("Описание2");
        film2.setDuration(120);
        film2.setReleaseDate(LocalDate.of(1999, 10, 10));
        film2.setGenres(Set.of(drama));
        film2.setMpa(g);

        film3 = new Film();
        film3.setId(3);
        film3.setName("Матрица3");
        film3.setDescription("Описание3");
        film3.setDuration(120);
        film3.setReleaseDate(LocalDate.of(1998, 10, 10));
        film3.setGenres(Set.of(drama, cartoon));
        film3.setMpa(g);

        film4 = new Film();
        film4.setId(4);
        film4.setName("Матрица4");
        film4.setDescription("Описание");
        film4.setDuration(120);
        film4.setReleaseDate(LocalDate.of(2000, 10, 10));
        film4.setGenres(Set.of(comedy));
        film4.setMpa(g);
        film4.setRate(0);
        film4.setLikes(0);

        film5 = new Film();
        film5.setId(5);
        film5.setName("Матрица4");
        film5.setDescription("Описание");
        film5.setDuration(120);
        film5.setReleaseDate(LocalDate.of(2000, 10, 10));
        film5.setGenres(Set.of(drama));
        film5.setMpa(g);
        film5.setRate(0);
        film5.setLikes(0);

        filmService.create(film1);
        filmService.create(film2);
        filmService.create(film3);
        filmService.create(film4);
        filmService.create(film5);
        likeService.addLike(1, 1);
    }

    void userInit() {
        user1 = new User();
        user1.setId(1);
        user1.setName("name");
        user1.setBirthday(LocalDate.of(1987, 4, 25));
        user1.setLogin("login");
        user1.setEmail("email@email.com");
        user1.setFriends(new HashSet<>());
        userService.create(user1);
    }

    @Test
    void getPopularFilmsYearAndGenreFiltered() {
        userInit();
        filmInit();
        List<Film> testFilmsList = filmService.getPopularFilms(10, 1, 2000);
        Assertions.assertEquals(testFilmsList, List.of(film1, film4));

        testFilmsList = filmService.getPopularFilms(10, null, 2000);
        Assertions.assertEquals(testFilmsList, List.of(film1, film4, film5));

        testFilmsList = filmService.getPopularFilms(10, null, null);
        Assertions.assertEquals(testFilmsList.size(), 5);
    }
}
