package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.ShortBooking;
import ru.practicum.shareit.comment.CommentResponseDto;

import java.util.List;
import java.util.Objects;

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
    private Long requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemResponseDto that = (ItemResponseDto) o;
        return available == that.available && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(ownerId, that.ownerId) && Objects.equals(nextBooking, that.nextBooking) && Objects.equals(lastBooking, that.lastBooking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available, ownerId, nextBooking, lastBooking, comments, requestId);
    }
}
