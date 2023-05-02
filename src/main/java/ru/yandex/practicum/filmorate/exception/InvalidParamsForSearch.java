package ru.yandex.practicum.filmorate.exception;

public class InvalidParamsForSearch extends RuntimeException {
    public InvalidParamsForSearch(String message) {
        super(message);
    }
}
