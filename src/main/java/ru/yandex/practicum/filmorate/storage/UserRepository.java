package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserRepository {

        private int idGenerator;
        private final Map<Integer, User> users = new HashMap<>();

        public Collection<User> getAll() {
                return users.values();
        }

        public void add(User user) {
                if(user.getName() == null) {
                        user.setName(user.getLogin());
                }
                if (user.getId() == null || user.getId() <= 0) {
                        user.setId(getIdGenerator());
                }
                if(userValuesIsValid(user)) {
                        users.put(user.getId(), user);
                }
        }

        public void update(User user) {
                if (!users.containsKey(user.getId())) {
                        throw new ValidationException("User not found.");
                }
                if(user.getName() == null) {
                        user.setName(user.getLogin());
                }
                if(userValuesIsValid(user)) {
                        users.put(user.getId(), user);
                }
        }

        private boolean userValuesIsValid(User user) {
                if(user.getLogin().contains(" ")) {
                        throw new ValidationException("The login must not contain whitespaces.");
                }
                return true;
        }

        private Integer getIdGenerator() {
                return ++idGenerator;
        }
}
