package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    @Transactional
    @Override
    public User saveUser(UserDto user) {
        log.info("A request was received to save user");
        return userRepository.save(UserMapper.mapToUser(user));
    }

    @Transactional
    @Override
    public User editUser(long userId, UserDto user) {
        log.info("A request was received to edit user with id " + userId);
        User updateUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User was not found"));
        Optional.ofNullable(user.getName()).ifPresent(updateUser::setName);
        if (Optional.ofNullable(user.getEmail()).isPresent()) {
            findUserByEmail(UserMapper.mapToUser(user));
            updateUser.setEmail(user.getEmail());
        }
        return userRepository.saveAndFlush(updateUser);
    }

    @Transactional
    @Override
    public void deleteUser(long userId) {
        log.info("A request was received to delete user with id " + userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("A request was received to get all users");
        return userRepository.findAll();
    }

    @Override
    public User getUserById(long userId) {
        log.info("A request was received to receive user by id " + userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + "was not found"));
    }

    private void findUserByEmail(User user) {
        if (userRepository.findAll().stream().anyMatch(x -> x.getEmail().equals(user.getEmail()))) {
            throw new AlreadyExistException("User with this email already exist");
        }
    }
}
