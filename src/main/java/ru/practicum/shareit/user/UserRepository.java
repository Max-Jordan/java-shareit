package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User saveUser(User user);

    void deleteUser(long userId);

    User editUser(long userId, User user);

    Optional<User> getUser(long userId);

    List<User> getAllUsers();
}
