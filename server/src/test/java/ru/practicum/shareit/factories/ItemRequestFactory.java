package ru.practicum.shareit.factories;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class ItemRequestFactory {

    public static ItemRequest makeItemRequest(long id, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setTimeCreate(LocalDateTime.now());
        itemRequest.setDescription("Description");
        itemRequest.setId(id);
        itemRequest.setRequester(user);
        return itemRequest;
    }

    public static ItemRequestDto makeItemRequestDto(long id) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Description");
        return dto;
    }

    public static ResponseItemRequestDto makeResponseItemRequestDto(ItemRequest request) {
        ResponseItemRequestDto response = new ResponseItemRequestDto();
        response.setCreated(request.getTimeCreate());
        response.setDescription(request.getDescription());
        response.setId(request.getId());
        return response;
    }
}
