package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {


    private ItemService itemService;

    private static final String SHARER_HEADER = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemResponseDto> getItemsByUser(@RequestParam(name = "from") Integer index,
                                                @RequestParam(name = "size") Integer size,
                                                @RequestHeader(SHARER_HEADER) long ownerId) {
        return itemService.getItemsByUser(ownerId, index, size);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItem(@PathVariable long itemId, @RequestHeader(SHARER_HEADER) Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @PostMapping
    public ItemResponseDto saveItem(@RequestHeader(SHARER_HEADER) long ownerId,
                                    @RequestBody ItemDto item) {
        return itemService.saveItem(ownerId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto editItem(@RequestHeader(SHARER_HEADER) long ownerId,
                                    @PathVariable long itemId,
                                    @RequestBody ItemDto item) {
        return itemService.editItem(ownerId, itemId, item);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> getItemBySearch(
            @RequestHeader(SHARER_HEADER) Long userId,
            @RequestParam(name = "text") String text,
            @RequestParam(name = "from") Integer index,
            @RequestParam(name = "size") Integer size) {
        return itemService.getItemBySearch(text, userId, index, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto publicCommentToItem(
            @PathVariable Long itemId,
            @RequestHeader(SHARER_HEADER) Long userId,
            @RequestBody CommentDto dto) {
        return itemService.addComment(itemId, userId, dto);
    }
}
