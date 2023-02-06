package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserDto user);

    void deleteUser(long userId);

    UserDto editUser(long userId, UserDto user);

    List<UserDto> getAllUsers();

    UserDto getUserById(long id);
}
