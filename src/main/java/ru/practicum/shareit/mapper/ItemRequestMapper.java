package ru.practicum.shareit.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    public static ItemRequest mapToItemRequest(ItemRequestDto dto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(dto.getDescription());
        return itemRequest;
    }

    public static ResponseItemRequestDto mapToResponseDto(ItemRequest itemRequest) {
        ResponseItemRequestDto dto = new ResponseItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setCreated(itemRequest.getTimeCreate());
        dto.setDescription(itemRequest.getDescription());
        return dto;
    }
}
