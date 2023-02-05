package ru.practicum.shareit.item;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemCustomRepository {

    private final ItemRepository itemRepository;

    public ItemRepositoryImpl(@Lazy ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> getItemsBySearch(String name) {
        if (StringUtils.isEmpty(name)) {
            return Collections.emptyList();
        }
        return itemRepository.findAll().stream()
                .filter(x -> BooleanUtils.isTrue(x.getAvailable()) && (
                        StringUtils.containsIgnoreCase(x.getName(), name) ||
                                StringUtils.containsIgnoreCase(x.getDescription(), name)))
                .collect(Collectors.toList());
    }
}
