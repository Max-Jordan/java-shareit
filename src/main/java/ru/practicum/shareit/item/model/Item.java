package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private Long id;
    private Long ownerId;
    @NotNull
    @NotBlank(message = "Enter the name")
    private String name;
    @NotNull
    @NotBlank(message = "Enter the description")
    private String description;
    @NotNull
    private Boolean available;
}
