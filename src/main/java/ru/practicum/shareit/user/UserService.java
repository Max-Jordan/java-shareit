package ru.practicum.shareit.user;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    void deleteUser(long userId);

    User editUser(long userId, User user);

    List<User> getAllUsers();

    User getUserById(long id);

    List<Item> getItemsByUser(long ownerId);

    void addItem(User user, Item item);

    void editItem(User user, Item item);
}
