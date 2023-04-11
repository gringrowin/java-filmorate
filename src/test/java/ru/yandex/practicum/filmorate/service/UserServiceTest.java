package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserStorage userStorage;

    private final User testUser = new User();

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
        Collection<User> usersExcept = List.of(testUser);
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

    @Test
    void getUserWhenIdNotFoundThenThrowUserNotFoundException() {
        int id = testUser.getId();
        when(userStorage.getUser(id)).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.getUser(id));
    }

    @Test
    void getUserWhenCorrectIdThenReturnedUser() {
        User userExcept = testUser;
        int id = testUser.getId();
        when(userStorage.getUser(id)).thenReturn(userExcept);

        User user = userService.getUser(id);

        verify(userStorage).getUser(id);
        assertEquals(userExcept, user);
    }

    @Test
    void addFriendWhenCorrectIdThenReturnedUser() {
        User user = testUser;
        int idUser = user.getId();

        User friend = new User();
        friend.setId(2);
        int idFriend = friend.getId();

        when(userStorage.getUser(idUser)).thenReturn(user);
        when(userStorage.getUser(idFriend)).thenReturn(friend);

        userService.addFriend(idUser, idFriend);

        verify(userStorage).getUser(idUser);
        verify(userStorage).getUser(idFriend);

        assertEquals(1, user.getFriends().size());
        assertEquals(1, friend.getFriends().size());
        assertTrue(user.getFriends().contains(friend.getId()));
        assertTrue(friend.getFriends().contains(user.getId()));
    }

    @Test
    void deleteFriendWhenCorrectIdThenReturnedUser() {
        User user = testUser;
        int idUser = user.getId();

        User friend = new User();
        friend.setId(2);
        int idFriend = friend.getId();
        user.getFriends().add(idFriend);
        friend.getFriends().add(idUser);

        when(userStorage.getUser(idUser)).thenReturn(user);
        when(userStorage.getUser(idFriend)).thenReturn(friend);

        userService.deleteFriend(idUser, idFriend);

        verify(userStorage).getUser(idUser);
        verify(userStorage).getUser(idFriend);

        assertEquals(0, user.getFriends().size());
        assertEquals(0, friend.getFriends().size());
        assertFalse(user.getFriends().contains(friend.getId()));
        assertFalse(friend.getFriends().contains(user.getId()));
    }
}