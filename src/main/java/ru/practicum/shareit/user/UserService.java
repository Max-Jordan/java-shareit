package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    void deleteUser(long userId);

    User editUser(long userId, User user);

    List<User> getAllUsers();

    User getUserById(long id);
}
