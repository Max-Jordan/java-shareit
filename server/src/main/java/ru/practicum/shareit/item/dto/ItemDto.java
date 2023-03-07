package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {

    private Long id;

    @NotBlank(message = "Please enter the name")
    private String name;
    @NotBlank(message = "Please enter the description")
    private String description;
    @NotNull
    private Boolean available;

    @Positive(message = "Request id can't be negative")
    private Long requestId;
}
