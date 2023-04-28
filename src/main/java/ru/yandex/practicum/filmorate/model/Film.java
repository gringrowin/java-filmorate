package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.constraints.FilmReleaseDateConstraint;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200, message = "Description length must be not more 200 characters")
    private String description;
    @FilmReleaseDateConstraint
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private Mpa mpa;
    private Integer rate;
    private Integer likes;
    private Set<Genre> genres = new HashSet<>();
}
