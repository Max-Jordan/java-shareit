package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.mapper.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.factories.UserFactory.makeUser;
import static ru.practicum.shareit.factories.UserFactory.makeUserDto;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private final UserDto dto = makeUserDto(1L);

    private final User user = makeUser(1L);

    @Test
    void getAllUsers_shouldReturnListUsers() {
        List<User> users = List.of(makeUser(1L), makeUser(2L));
        when(userRepository.findAll()).thenReturn(users);

        assertThat(userService.getAllUsers().stream()
                .map(UserMapper::mapToUser).collect(Collectors.toList()), equalTo(users));
    }

    @Test
    void getUserById_shouldReturnUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThat(UserMapper.mapToUser(userService.getUserById(1)), equalTo(user));
    }

    @Test
    void getUserById_shouldReturnException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(1)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void editUser_shouldReturnNewUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.saveAndFlush(any())).thenReturn(user);

        assertThat(UserMapper.mapToUser(userService.editUser(1, dto)), equalTo(user));
    }

    @Test
    void editUser_shouldReturnException() {
        when(userRepository.findAll()).thenReturn(List.of(makeUser(1), makeUser(2)));
        assertThatThrownBy(() -> userService.editUser(1, dto)).isInstanceOf(NotFoundException.class);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.editUser(1, dto)).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    void saveUser_shouldReturnUser() {
        when(userRepository.save(any())).thenReturn(user);
        assertThat(UserMapper.mapToUser(userService.saveUser(dto)), equalTo(user));
    }
}