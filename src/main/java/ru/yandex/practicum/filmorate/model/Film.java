package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.constraints.FilmReleaseDateConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private Set<Director> directors = new HashSet<>();
}
