package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    @NotBlank(message = "Name can't be blank")
    private String name;
    @Size(max = 500)
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    @Positive(message = "Request id must be positive")
    private Long requestId;

}
