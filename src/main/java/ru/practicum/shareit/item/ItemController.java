package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Validated
public class ItemController {


    private ItemService itemService;

    private static final String SHARER_HEADER = "X-Sharer-User-Id";

    @GetMapping
    public List<Item> getItemsByUser(@RequestHeader(SHARER_HEADER) long ownerId) {
        return itemService.getItemsByUser(ownerId);
    }

    @GetMapping("/{itemId}")
    public Item getItem(@PathVariable long itemId, @RequestHeader(SHARER_HEADER) Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @PostMapping
    public Item saveItem(@RequestHeader(SHARER_HEADER) long ownerId,
                            @Valid
                            @RequestBody ItemDto item) {
        return itemService.saveItem(ownerId, item);
    }

    @PatchMapping("/{itemId}")
    public Item editItem(@RequestHeader(SHARER_HEADER) long ownerId,
                            @PathVariable long itemId,
                            @RequestBody ItemDto item) {
        return itemService.editItem(ownerId, itemId, item);
    }

    @GetMapping("/search")
    public List<Item> getItemBySearch(
            @RequestParam String text) {
        return itemService.getItemBySearch(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto publicCommentToItem(
            @PathVariable Long itemId,
            @RequestHeader(SHARER_HEADER) Long userId,
            @Valid@RequestBody CommentDto dto) {
        return itemService.addComment(itemId, userId, dto);
    }
}
