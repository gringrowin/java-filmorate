package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private Integer id;
    @NotBlank
    private final String name;
    @Size(max = 200, message = "Description length must be not more 200 characters")
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final Integer duration;
    private Integer rate;

}
