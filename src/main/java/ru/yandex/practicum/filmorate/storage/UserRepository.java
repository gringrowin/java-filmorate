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
                setName(user);
                if (user.getId() == null || user.getId() <= 0) {
                        user.setId(getIdGenerator());
                }
                users.put(user.getId(), user);

        }

        public void update(User user) {
                if (!users.containsKey(user.getId())) {
                        throw new ValidationException("User with id " + user.getId() + " not found.");
                }
                setName(user);
                users.put(user.getId(), user);

        }

        private void setName(User user) {
                if(user.getName() == null) {
                        user.setName(user.getLogin());
                }
        }

        private Integer getIdGenerator() {
                return ++idGenerator;
        }
}
