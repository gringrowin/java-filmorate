package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.OperationType;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class Feed {
    @NotNull
    private Long timestamp;
    @NotNull
    private Long userId;
    @NotNull
    private OperationType operation;
    @NotNull
    private Long eventId;
    @NotNull
    private Long entityId;
    @NotNull
    private EventType eventType;
}

