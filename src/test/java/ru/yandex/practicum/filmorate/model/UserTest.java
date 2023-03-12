package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }


    @Test
    void validateName() {
        User user = new User();
        user.setName("  ");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Name is empty");
    }

    @Test
    void validateEmailIfBlank() {
        User user = new User();
        user.setEmail(" ");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(2, violations.size(), "Email is blank");
    }

    @Test
    void validateEmailIfNotCorrectFormat() {
        User user = new User();
        user.setEmail("mamama@mmm");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Email format is incorrect");
    }

    @Test
    void validateLogin() {
        User user = new User();
        user.setLogin("Senior Pomidor");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Login incorrect");
    }

    @Test
    void validateBirthday() {
        User user = new User();
        user.setLogin("mama");
        user.setBirthday(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Birthday must be past then now");
    }
}