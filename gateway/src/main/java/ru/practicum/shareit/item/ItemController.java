package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private static final String X_SHARER_HEADER = "X-Sharer-User-Id";
    private static final String MESSAGE_FOR_INDEX = "The index can't be negative";
    private static final String MESSAGE_FOR_SIZE = "The size can't be negative";

    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> saveItem(@RequestHeader(X_SHARER_HEADER) long userId,
                                           @Valid @RequestBody ItemDto dto) {
        return client.saveItem(userId, dto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable long itemId, @RequestHeader(X_SHARER_HEADER) long userId) {
        return client.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUser(@RequestHeader(X_SHARER_HEADER) long userId,
                                                 @RequestParam(name = "index", defaultValue = "0", required = false)
                                                 @Min(value = 0, message = MESSAGE_FOR_INDEX) long index,
                                                 @RequestParam(name = "size", required = false, defaultValue = "10")
                                                 @Min(value = 0, message = MESSAGE_FOR_SIZE) long size) {
        return client.getItemsByUser(index, size, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsBySearch(@RequestHeader(X_SHARER_HEADER) Long userId,
                                                   @RequestParam String text,
                                                   @RequestParam(name = "from", required = false, defaultValue = "0")
                                                   @Min(value = 0, message = MESSAGE_FOR_INDEX) Integer index,
                                                   @RequestParam(name = "size", required = false, defaultValue = "20")
                                                   @Min(value = 1, message = MESSAGE_FOR_SIZE) Integer size) {
        return client.getItemBySearch(text, userId, index, size);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> editItem(@RequestBody ItemDto dto, @PathVariable long itemId,
                                           @RequestHeader(X_SHARER_HEADER) long userId) {
        return client.editItem(dto, itemId, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable long itemId, @RequestHeader(X_SHARER_HEADER) long userId,
                                                @Valid@RequestBody CommentDto dto) {
        return client.postComment(itemId, userId, dto);
    }
}
