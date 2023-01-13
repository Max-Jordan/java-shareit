package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item saveItem(Item item);

    Item editItem(Long itemId, Item item);

    Optional<Item> getItem(Long itemId);

    List<Item> getItemBySearch(String name);
}
