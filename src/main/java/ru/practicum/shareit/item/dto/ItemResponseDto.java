package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.ShortBooking;
import ru.practicum.shareit.comment.CommentResponseDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {

    private Long id;
    private String name;
    private String description;

    private boolean available;
    private Long ownerId;
    private ShortBooking nextBooking;
    private ShortBooking lastBooking;
    private List<CommentResponseDto> comments;
}
