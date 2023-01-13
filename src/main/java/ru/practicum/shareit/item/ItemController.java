package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {


    private ItemService itemService;

    @GetMapping
    public List<ItemDto> getItemsByUser(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.getItemsByUser(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @PostMapping
    public ItemDto saveItem(@RequestHeader("X-Sharer-User-Id") long ownerId,
                            @Valid
                            @RequestBody Item item) {
        return itemService.saveItem(ownerId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@RequestHeader("X-Sharer-User-Id") long ownerId,
                            @PathVariable long itemId,
                            @RequestBody Item item) {
        return itemService.editItem(ownerId, itemId, item);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemBySearch(
            @RequestParam String text) {
        return itemService.getItemBySearch(text);
    }
}
