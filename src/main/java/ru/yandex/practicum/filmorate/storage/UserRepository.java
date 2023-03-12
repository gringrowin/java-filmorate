package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UserRepository {

        private int idGenerator;
        private final Map<Integer, User> users = new HashMap<>();

        public Collection<User> getAll() {
                return users.values();
        }

        public void add(User user) {
                setNameAndId(user);
                if (users.containsValue(user)) {
                        log.warn("user already exists: {}", user);
                        throw new ValidationException("User with " + user.getName() + " already exists.");
                }
                users.put(user.getId(), user);
        }

        public void update(User user) {
                setNameAndId(user);
                if (!users.containsKey(user.getId())) {
                        log.warn("user not found: {}", user);
                        throw new ValidationException("User with id " + user.getId() + " not found.");
                }
                users.put(user.getId(), user);

        }

        private void setNameAndId(User user) {
                if(user.getName() == null) {
                        user.setName(user.getLogin());
                        log.info("setNameAndId: {} - setName", user);
                }
                if (user.getId() == null || user.getId() <= 0) {
                        user.setId(getIdGenerator());
                        log.info("setNameAndId: {} - setId", user);
                }
        }

        private Integer getIdGenerator() {
                return ++idGenerator;
        }

        public int size() {
                return users.size();
        }
}
