package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping(path = "/users")
@RestController
@Validated
public class UserController {

    private final UserClient userClint;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClint.getAllUser();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {
        return userClint.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@Valid @RequestBody UserDto user) {
        return userClint.saveUser(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> editUser(@PathVariable long userId, @RequestBody UserDto user) {
        return userClint.editUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userClint.deleteUser(userId);
    }
}
