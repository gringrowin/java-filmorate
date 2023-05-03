package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    private final FilmStorage filmStorage;

    private final FriendStorage friendStorage;

    private final LikeStorage likeStorage;

    @Autowired
    public UserService(@Qualifier("dbUserStorage") UserStorage userStorage,
                       @Qualifier("dbFilmStorage") FilmStorage filmStorage,
                       FriendStorage friendStorage,
                       LikeStorage likeStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
        this.likeStorage = likeStorage;
        this.filmStorage = filmStorage;
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
        user.setFriends(friendStorage.getFriends(id));
        return user;
    }

    public User addFriend(Integer userId, Integer friendId) {
        log.info("addFriend: {} - Started", friendId);
        User user = friendStorage.addFriend(getUser(userId), getUser(friendId));
        log.info("addFriend: {} - Finished", user);
        return getUser(userId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        log.info("deleteFriend: {} - Started", friendId);
        User user = friendStorage.deleteFriend(getUser(userId), getUser(friendId));
        log.info("deleteFriend: {} - Finished", user);
        return getUser(userId);
    }

    public List<User> getFriends(Integer userId) {
        log.info("getFriends: {} - ", getUser(userId));
        return getUser(userId)
                .getFriends()
                .stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        Set<Integer> userFriends = getUser(userId).getFriends();
        log.info("getCommonFriends: {} - Started", userFriends);
        Set<Integer> otherFriends = getUser(otherId).getFriends();
        log.info("getCommonFriends: {} - Started", otherFriends);
        if (userFriends == null || otherFriends == null) {
            return Collections.emptyList();
        }
        Set<Integer> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(otherFriends);
        log.info("getCommonFriends: {} - Finished", commonFriends);
        return commonFriends.stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    private void checkNameUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public List<Film> getRecommendations(int userId){
        List<Integer> userFilms = likeStorage.getLikedFilmsByUserId(userId);
        List<User> users = findAll();
        HashMap<Integer, List<Integer>> likes = new HashMap<>();
        for (User user : users) {
            if(user.getId() != userId){
                likes.put(user.getId(), likeStorage.getLikedFilmsByUserId(user.getId()));
            }
        }
        int maxCommonElementsCount = 0;
        List<Integer> films = new ArrayList<>();
        for (Integer anotherUserId : likes.keySet()) {
            List<Integer> likedFilms = likes.get(anotherUserId);
            int commonSum = 0;
            for (Integer filmId:userFilms) {
                for (Integer anotherFilmId : likedFilms) {
                    if(filmId == anotherFilmId){
                        commonSum++;
                    }
                }
            }
            if(commonSum>maxCommonElementsCount){
                maxCommonElementsCount = commonSum;
                films = likedFilms;
            }
        }
        films.removeAll(userFilms);
        System.out.println(films);
        List<Film> recommendations = new ArrayList<>();
        for (Integer filmId:films) {
            //recommendations.add(FilmService::getFilm,filmId);
        }
        return recommendations;
    }
}
