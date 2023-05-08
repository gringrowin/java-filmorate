package ru.yandex.practicum.filmorate.validation.constraints;

import ru.yandex.practicum.filmorate.validation.validators.FilmReleaseDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FilmReleaseDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FilmReleaseDateConstraint {
    String message() default "Invalid ReleaseDate: Release date must be after 28.12.1895.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
