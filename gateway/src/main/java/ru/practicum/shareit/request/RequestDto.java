package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RequestDto {

    @Size(min = 1, max = 1000)
    @NotNull
    private String description;
}
