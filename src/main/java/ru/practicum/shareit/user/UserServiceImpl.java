package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        log.info("A request was received to save user");
        findUserByEmail(user);
        return userRepository.saveUser(user);
    }

    @Override
    public User editUser(long userId, User user) {
        log.info("A request was received to edit user with id " + userId);
        User updateUser = userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            findUserByEmail(user);
            updateUser.setEmail(user.getEmail());
        }
        return userRepository.editUser(userId, updateUser);
    }

    @Override
    public void deleteUser(long userId) {
        log.info("A request was received to delete user with id " + userId);
        userRepository.deleteUser(userId);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("A request was received to get all users");
        return userRepository.getAllUsers();
    }

    @Override
    public User getUserById(long userId) {
        log.info("A request was received to receive user by id " + userId);
        return userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

    private void findUserByEmail(User user) {
        if (userRepository.getAllUsers().stream().anyMatch(x -> x.getEmail().equals(user.getEmail()))) {
            throw new AlreadyExistException("User with this email already exist");
        }
    }
}
