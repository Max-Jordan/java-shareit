package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private static final String SHARER_HEADER = "X-Sharer-User-Id";

    private final ItemRequestServiceImpl itemRequestService;

    @PostMapping
    public ResponseItemRequestDto addItemRequest(@RequestBody @Valid ItemRequestDto dto,
                                                 @RequestHeader(SHARER_HEADER) Long userId) {
        return itemRequestService.save(dto, userId);
    }

    @GetMapping
    public List<ResponseItemRequestDto> getResponsesByRequest(@RequestHeader(SHARER_HEADER) Long userId) {
        return itemRequestService.getResponses(userId);
    }

    @GetMapping("/all")
    public List<ResponseItemRequestDto> getRequestsByOtherUser(
            @RequestHeader(SHARER_HEADER) Long userId,
            @RequestParam(name = "from") Integer index,
            @RequestParam(name = "size") Integer size) {
        return itemRequestService.getRequestByOtherUser(userId, index, size);
    }

    @GetMapping("/{requestId}")
    public ResponseItemRequestDto getRequestById(
            @RequestHeader(SHARER_HEADER) Long userId,
            @PathVariable Long requestId) {
        return itemRequestService.getRequestById(requestId, userId);
    }
}
