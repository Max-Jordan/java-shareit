package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {

    @NotBlank(message = "Name can't be blank")
    private String name;

    @Email(message = "Please enter correct email")
    @NotNull
    private String email;
}
