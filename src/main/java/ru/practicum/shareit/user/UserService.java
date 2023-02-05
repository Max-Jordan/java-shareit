package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    User saveUser(UserDto user);

    void deleteUser(long userId);

    User editUser(long userId, UserDto user);

    List<User> getAllUsers();

    User getUserById(long id);
}
