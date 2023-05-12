package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    private Film createFilm() {
        Film film = new Film();
        film.setName("Kong");
        film.setDescription("Cool");
        film.setReleaseDate(LocalDate.of(1985, 12, 29));
        film.setDuration(120);
        return film;
    }

    @Test
    void validateName() {
        Film film = createFilm();
        film.setName("  ");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "incorrect film");
    }

    @Test
    void validateDescription() {
        Film film = createFilm();
        film.setDescription("d".repeat(201));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Description is more then 200 char");
    }

    @Test
    void validateDuration() {
        Film film = createFilm();
        film.setDuration(-1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "duration is negative");
    }

    @Test
    void validateReleaseDate() {
        Film film = createFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "duration is negative");
    }
}