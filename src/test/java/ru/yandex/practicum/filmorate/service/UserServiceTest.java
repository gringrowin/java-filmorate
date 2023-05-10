package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final User testUser = new User();
    @InjectMocks
    private UserService userService;
    @Mock
    private UserStorage userStorage;
    @Mock
    private FilmService filmService;
    @Mock
    private LikeService likeService;

    @BeforeEach
    void initializationTestUser() {
        testUser.setId(1);
        testUser.setName("name");
        testUser.setBirthday(LocalDate.of(2000, 1, 1));
        testUser.setLogin("login");
        testUser.setEmail("mail@mail.ru");
    }

    @Test
    void findAllWhenStorageNotEmptyThenReturnedList() {
        List<User> usersExcept = List.of(testUser);
        when(userStorage.getAll()).thenReturn(usersExcept);

        Collection<User> users = userService.findAll();

        verify(userStorage).getAll();
        assertEquals(usersExcept, users);
    }

    @Test
    void createUserWhenReturnedUser() {
        User userExcept = testUser;
        when(userStorage.add(userExcept)).thenReturn(userExcept);

        User user = userService.create(testUser);

        verify(userStorage).add(user);
        assertEquals(userExcept, user);
    }

    @Test
    void updateUserWhenReturnedUpdatedUser() {
        User userExcept = testUser;
        when(userStorage.update(userExcept)).thenReturn(userExcept);

        User user = userService.update(testUser);

        verify(userStorage).update(testUser);
        assertEquals(userExcept, user);
    }
}