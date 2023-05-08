package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.OperationType;
import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

public interface FeedStorage {

    List<Feed> getFeed(int userId);

    void addFeedEvent(EventType eventType, OperationType operationType, int userId, int entityId);
}
