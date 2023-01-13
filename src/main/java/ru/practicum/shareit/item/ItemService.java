package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto saveItem(Long ownerId, Item item);

    List<ItemDto> getItemsByUser(Long ownerId);

    ItemDto getItemById(Long itemId);

    ItemDto editItem(Long ownerId, Long itemId, Item item);

    List<ItemDto> getItemBySearch(String name);
}
