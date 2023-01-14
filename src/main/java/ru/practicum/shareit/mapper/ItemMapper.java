package ru.practicum.shareit.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@NoArgsConstructor
public class ItemMapper {

    public static ItemDto mapItemToDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }
}
