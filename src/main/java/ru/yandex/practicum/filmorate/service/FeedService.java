package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.OperationType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.util.List;

@Service
@Slf4j
public class FeedService {
    private final FeedStorage feedStorage;

    @Autowired
    public FeedService(FeedStorage feedStorage) {
        this.feedStorage = feedStorage;
    }

    public List<Feed> getFeed(int userId) {
        return feedStorage.getFeed(userId);
    }

    public void addFeedEvent(EventType eventType, OperationType operationType, int userId, int entityId) {
        feedStorage.addFeedEvent(eventType, operationType, userId, entityId);
    }
}
