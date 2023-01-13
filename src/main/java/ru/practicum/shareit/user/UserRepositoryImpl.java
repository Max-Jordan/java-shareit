package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();

    private Long id = 0L;

    @Override
    public User saveUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(long id) {
        users.remove(id);
    }

    @Override
    public User editUser(long userId, User user) {
        users.replace(userId, user);
        return user;
    }

    @Override
    public Optional<User> getUser(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User with id " + id + " was not found");
        }
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

}
