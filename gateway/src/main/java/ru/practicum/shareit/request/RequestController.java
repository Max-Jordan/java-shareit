package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class RequestController {

    private static final String X_SHARER_USER = "X-Sharer-User-Id";

    private final RequestClient client;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestBody @Valid RequestDto dto,
                                                 @RequestHeader(X_SHARER_USER) Long userId) {
        return client.save(dto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getResponsesByRequest(@RequestHeader(X_SHARER_USER) Long userId) {
        return client.getResponses(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsByOtherUser(
            @RequestHeader(X_SHARER_USER) Long userId,
            @RequestParam(name = "from", required = false, defaultValue = "0")
            @Min(value = 0, message = "Index can't be negative")
            Integer index,
            @RequestParam(name = "size", required = false, defaultValue = "20")
            @Min(value = 1, message = "Size can't be less than one") Integer size) {
        return client.getAllRequests(userId, index, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(
            @RequestHeader(X_SHARER_USER) Long userId,
            @PathVariable Long requestId) {
        return client.getRequestById(requestId, userId);
    }
}

