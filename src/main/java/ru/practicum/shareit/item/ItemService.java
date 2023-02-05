package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item saveItem(Long ownerId, ItemDto item);

    List<Item> getItemsByUser(Long ownerId);

    Item getItemById(Long itemId, Long userId);

    Item editItem(Long ownerId, Long itemId, ItemDto item);

    List<Item> getItemBySearch(String name);

    CommentResponseDto addComment(Long itemId, Long userId, CommentDto dto);
}
