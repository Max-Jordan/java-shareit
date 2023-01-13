package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final HashMap<Long, Item> itemStore;
    private Long id = 0L;

    @Override
    public Item saveItem(Item item) {
        item.setId(++id);
        itemStore.put(item.getId(), item);
        return item;
    }

    @Override
    public Item editItem(Long itemId, Item item) {
        itemStore.replace(itemId, item);
        return item;
    }

    @Override
    public Optional<Item> getItem(Long itemId) {
        return Optional.ofNullable(itemStore.get(itemId));
    }

    @Override
    public List<Item> getItemBySearch(String name) {
        if (StringUtils.isEmpty(name)) {
            return Collections.emptyList();
        }
        return itemStore.values().stream()
                .filter(x -> BooleanUtils.isTrue(x.getAvailable()) && (
                        x.getName().toLowerCase().contains(name.toLowerCase()) ||
                                x.getDescription().toLowerCase().contains(name.toLowerCase())))
                .collect(Collectors.toList());
    }
}
