package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Validated
public class ItemController {


    private ItemService itemService;

    private static final String SHARER_HEADER = "X-Sharer-User-Id";

    private static final String MESSAGE_FOR_INDEX = "The index can't be negative";
    private static final String MESSAGE_FOR_SIZE = "The size can't be less than one";

    @GetMapping
    public List<ItemResponseDto> getItemsByUser(@RequestParam(name = "from", required = false, defaultValue = "0")
                                                @Min(value = 0, message = MESSAGE_FOR_INDEX) Integer index,
                                                @RequestParam(name = "size", required = false, defaultValue = "20")
                                                @Min(value = 1, message = MESSAGE_FOR_SIZE) Integer size,
                                                @RequestHeader(SHARER_HEADER) long ownerId) {
        return itemService.getItemsByUser(ownerId, index, size);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItem(@PathVariable long itemId, @RequestHeader(SHARER_HEADER) Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @PostMapping
    public ItemResponseDto saveItem(@RequestHeader(SHARER_HEADER) long ownerId,
                                    @Valid
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
            @RequestParam String text,
            @RequestParam(name = "from", required = false, defaultValue = "0")
            @Min(value = 0, message = MESSAGE_FOR_INDEX) Integer index,
            @RequestParam(name = "size", required = false, defaultValue = "20")
            @Min(value = 1, message = MESSAGE_FOR_SIZE) Integer size) {
        return itemService.getItemBySearch(text, index, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto publicCommentToItem(
            @PathVariable Long itemId,
            @RequestHeader(SHARER_HEADER) Long userId,
            @Valid @RequestBody CommentDto dto) {
        return itemService.addComment(itemId, userId, dto);
    }
}
