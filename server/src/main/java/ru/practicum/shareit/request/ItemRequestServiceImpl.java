package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.mapper.ItemRequestMapper;
import ru.practicum.shareit.mapper.PaginationMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Override
    public ResponseItemRequestDto save(ItemRequestDto dto, Long userId) {
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(dto);
        itemRequest.setRequester(getUserFromRepository(userId));
        itemRequest.setTimeCreate(LocalDateTime.now());
        return ItemRequestMapper.mapToResponseDto(repository.save(itemRequest));
    }

    @Override
    public List<ResponseItemRequestDto> getResponses(Long userId) {
        return setItems(repository.findAllByRequester(getUserFromRepository(userId))
                .stream()
                .map(ItemRequestMapper::mapToResponseDto)
                .collect(toList()));
    }

    @Override
    public List<ResponseItemRequestDto> getRequestByOtherUser(Long userId, Integer index, Integer size) {
        return setItems(repository.findItemRequestByRequesterIsNotOrderByTimeCreateDesc(getUserFromRepository(userId),
                        PaginationMapper.mapToPageable(index, size))
                .stream()
                .map(ItemRequestMapper::mapToResponseDto)
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseItemRequestDto getRequestById(Long id, Long ownerId) {
        getUserFromRepository(ownerId);
        ResponseItemRequestDto dto = ItemRequestMapper.mapToResponseDto(repository.findById(id).orElseThrow(() -> new NotFoundException("The request was not found")));
        dto.setItems(itemRepository.findAllByRequestId(id).stream()
                .map(ItemMapper::mapToItemResponseDto)
                .collect(toList()));
        return dto;
    }


    private User getUserFromRepository(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not exist"));
    }

    private List<ResponseItemRequestDto> setItems(List<ResponseItemRequestDto> itemRequest) {
        return itemRequest.stream()
                .peek(i -> i.setItems(itemRepository.findAllByRequestId(i.getId())
                        .stream()
                        .map(ItemMapper::mapToItemResponseDto)
                        .collect(toList())))
                .collect(toList());
    }
}
