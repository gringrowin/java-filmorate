package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectorTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    private Director createDirector(int id) {
        return new Director(id, "Quentin Tarantino");
    }

    @DisplayName("Проверка корректности ID режиссёра")
    @Test
    void validateDirectorId() {
        Director director = createDirector(-10);

        Set<ConstraintViolation<Director>> violations = validator.validate(director);
        assertEquals(1, violations.size(), "Некорректный id режиссёра");
    }

    @DisplayName("Проверка корректности имени режиссёра по параметру длины (100 симв.) имени")
    @Test
    void validateDirectorNameByLength() {
        Director director = createDirector(1).withName("Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                "Present qui nunc non libero consectetur ullamcorper sedvel orci.");

        Set<ConstraintViolation<Director>> violations = validator.validate(director);
        assertEquals(1, violations.size(), "Имя режиссёра не может быть длиннее 100 символов");
    }

    @DisplayName("Проверка корректности имени режиссёра по параметру пустоты имени")
    @Test
    void validateDirectorNameByBlank() {
        Director director = createDirector(1).withName("            ");

        Set<ConstraintViolation<Director>> violations = validator.validate(director);
        assertEquals(1, violations.size(), "Имя режиссёра не может быть пустым!");
    }
}
