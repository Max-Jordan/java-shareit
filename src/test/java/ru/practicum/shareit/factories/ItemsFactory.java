package ru.practicum.shareit.factories;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemsFactory {

    public static ItemDto makeItemDto(long id) {
        return ItemDto.builder().id(id)
                .name("test")
                .description("test description")
                .available(true)
                .requestId(1L)
                .build();
    }

    public static ItemResponseDto makeItemResponseDto(ItemDto dto, long id) {
        ItemResponseDto responseDto = new ItemResponseDto();
        responseDto.setRequestId(dto.getRequestId());
        responseDto.setOwnerId(id);
        responseDto.setDescription(dto.getDescription());
        responseDto.setName(dto.getName());
        responseDto.setId(dto.getId());
        responseDto.setAvailable(dto.getAvailable());
        responseDto.setComments(new ArrayList<>());
    return responseDto;
    }

    public static Item makeItem(long id) {
        Item item = new Item();
        item.setName("test");
        item.setAvailable(true);
        item.setDescription("test description");
        item.setIdOwner(1L);
        item.setRequestId(2L);
        item.setId(id);
        return item;
    }
}
