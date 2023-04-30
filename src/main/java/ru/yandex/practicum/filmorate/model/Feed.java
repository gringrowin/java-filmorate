package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.OperationType;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class Feed {
    @NotNull
    private long timestamp;
    @NotNull
    private long userId;
    @NotNull
    private EventType eventType;
    @NotNull
    private OperationType operation;
    @NotNull
    private long eventId;
    @NotNull
    private long entityId;
}
