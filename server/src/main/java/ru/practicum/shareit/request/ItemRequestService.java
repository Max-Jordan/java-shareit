package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;

import java.util.List;

@Service
public interface ItemRequestService {

    ResponseItemRequestDto save(ItemRequestDto dto, Long userId);

    List<ResponseItemRequestDto> getResponses(Long userId);

    List<ResponseItemRequestDto> getRequestByOtherUser(Long userId, Integer index, Integer size);

    ResponseItemRequestDto getRequestById(Long id, Long ownerId);
}
