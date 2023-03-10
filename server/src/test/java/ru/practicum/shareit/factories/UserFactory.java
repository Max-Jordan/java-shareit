package ru.practicum.shareit.factories;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFactory {

    public static User makeUser(long id) {
        return User.builder()
                .id(id)
                .name("test name")
                .email("test@test.ru").build();
    }

    public static UserDto makeUserDto(long id) {
        return UserDto.builder().id(id)
                .name("test name")
                .email("test@test.ru").build();
    }
}
