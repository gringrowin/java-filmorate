package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.util.List;

@Service
@Slf4j
public class FeedService {
    private final FeedStorage feedStorage;
    private final UserService userService;

    @Autowired
    public FeedService(FeedStorage feedStorage,
                       UserService userService) {
        this.feedStorage = feedStorage;
        this.userService = userService;
    }

    public List<Feed> getFeed(int userId) {
        return feedStorage.getFeed(userId);
    }
}
