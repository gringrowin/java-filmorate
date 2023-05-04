package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@EqualsAndHashCode
@With
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Director {
    @NotNull
    @PositiveOrZero
    private Integer id;
    @Size(max = 100, message = "Имя режиссёра не может быть длиннее 100 символов")
    @NotBlank(message = "Имя режиссёра не может быть пустым!")
    private String name;
}
