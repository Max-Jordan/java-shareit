package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {

    ItemResponseDto saveItem(Long ownerId, ItemDto item);

    List<ItemResponseDto> getItemsByUser(Long ownerId);

    ItemResponseDto getItemById(Long itemId, Long userId);

    ItemResponseDto editItem(Long ownerId, Long itemId, ItemDto item);

    List<ItemResponseDto> getItemBySearch(String name);

    CommentResponseDto addComment(Long itemId, Long userId, CommentDto dto);
}
