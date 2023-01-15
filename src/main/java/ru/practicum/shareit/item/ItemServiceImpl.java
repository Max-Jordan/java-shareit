package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;

    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto saveItem(Long ownerId, Item item) {
        item.setOwnerId(ownerId);
        log.info("A request was received to save item");
        User owner = userRepository.getUser(ownerId)
                .orElseThrow(() -> new NotFoundException("Owner with this id not found"));
        Item newItem = itemRepository.saveItem(item);
        userService.addItem(owner, newItem);
        return ItemMapper.mapItemToDto(newItem);
    }

    @Override
    public List<ItemDto> getItemsByUser(Long ownerId) {
        log.info("A request was received to receive the user's items");
        return userService.getItemsByUser(ownerId).stream()
                .map(ItemMapper::mapItemToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        log.info("A request was received to receive item by id");
        return ItemMapper.mapItemToDto(itemRepository.getItem(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found")));
    }

    @Override
    public ItemDto editItem(Long ownerId, Long itemId, Item item) {
        log.info("A request was received to edit item with id " + itemId);
        Item updateItem = itemRepository.getItem(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
        if (!Objects.equals(updateItem.getOwnerId(), ownerId)) {
            throw new NotFoundException("This item does not belong to this user");
        }
        Optional.ofNullable(item.getDescription()).ifPresent(updateItem::setDescription);
        Optional.ofNullable(item.getName()).ifPresent(updateItem::setName);
        Optional.ofNullable(item.getAvailable()).ifPresent(updateItem::setAvailable);
        User owner = userRepository.getUser(ownerId)
                .orElseThrow(() -> new NotFoundException("Owner was not found"));
        userService.editItem(owner, updateItem);
        return ItemMapper.mapItemToDto(itemRepository.editItem(itemId, updateItem));
    }

    @Override
    public List<ItemDto> getItemBySearch(String name) {
        log.info("A request was received to receive item by name or description");
        return itemRepository.getItemBySearch(name).stream()
                .map(ItemMapper::mapItemToDto)
                .collect(Collectors.toList());
    }
}
