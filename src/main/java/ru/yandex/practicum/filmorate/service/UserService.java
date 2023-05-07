package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    private final FilmService filmService;

    private final LikeService likeService;

    private final FeedStorage feedStorage;

    @Autowired
    public UserService(@Qualifier("dbUserStorage") UserStorage userStorage,
                       FilmService filmService,
                       LikeService likeService),
                       @Qualifier("dbFeedStorage") FeedStorage feedStorage) {
        this.userStorage = userStorage;
        this.likeService = likeService;
        this.filmService = filmService;
        this.friendStorage = friendStorage;
        this.feedStorage = feedStorage;
    }


    public List<User> findAll() {
        List<User> users = userStorage.getAll();
        log.info("findAll: {}", users);
        return users;
    }

    public User create(User user) {
        checkNameUser(user);
        log.info("create: {} - Started", user);
        user = userStorage.add(user);
        log.info("create: {} - Finished", user);
        return user;
    }

    public User update(User user) {
        checkNameUser(user);
        log.info("update: {} - Started", user);
        user = userStorage.update(user);
        log.info("update: {} - Finished", user);
        return user;
    }

    public User getUser(Integer id) {
        User user = userStorage.getUser(id);
        log.info("getUser: {} - ", user);
        return user;
    }

    private void checkNameUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public List<Film> getRecommendations(int userId) {
        List<Integer> userFilms = likeService.getLikedFilmsByUserId(userId);
        List<User> users = findAll();
        HashMap<Integer, List<Integer>> likes = new HashMap<>();
        for (User user : users) {
            if (user.getId() != userId) {
                likes.put(user.getId(), likeService.getLikedFilmsByUserId(user.getId()));
            }
        }
        int maxCommonElementsCount = 0;
        List<Integer> films = new ArrayList<>();
        for (Integer anotherUserId : likes.keySet()) {
            List<Integer> likedFilms = likes.get(anotherUserId);
            int commonSum = 0;
            for (Integer filmId : userFilms) {
                for (Integer anotherFilmId : likedFilms) {
                    if (filmId == anotherFilmId) {
                        commonSum++;
                    }
                }
            }
            if (commonSum > maxCommonElementsCount) {
                maxCommonElementsCount = commonSum;
                films = likedFilms;
            }
        }
        films.removeAll(userFilms);
        List<Film> recommendations = new ArrayList<>();
        for (Integer filmId : films) {
            recommendations.add(filmService.getFilm(filmId));
        }
        return recommendations;
    }

    public void deleteUser(Integer userId) {
        log.info("deleteUser: {} - ", userId);
        userStorage.deleteUser(userId);
    }

    public List<Feed> getFeed(long userId) {
        return feedStorage.getFeed(userId);
    }
}
