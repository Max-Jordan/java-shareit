package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.factories.ItemRequestFactory.*;
import static ru.practicum.shareit.factories.ItemsFactory.*;
import static ru.practicum.shareit.factories.UserFactory.makeUser;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private ItemRequestDto dto;

    private ItemRequest request;

    private ResponseItemRequestDto response;

    @BeforeEach
    void setUp() {
        dto = makeItemRequestDto(1L);
        request = makeItemRequest(1L, makeUser(1L));
        response = makeResponseItemRequestDto(request);
    }


    @Test
    void save_shouldReturnItemRequest() {
        when(itemRequestRepository.save(any())).thenReturn(request);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1L)));

        assertThat(itemRequestService.save(dto, 1L).getId(), equalTo(response.getId()));
    }

    @Test
    void save_shouldReturnNotFoundException() {
        when(userRepository.findById(anyLong())).thenThrow(new NotFoundException("message"));

        assertThatThrownBy(() -> itemRequestService.save(dto, 1L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getResponses_shouldReturnListOfResponses() {
        ItemResponseDto responseDto = makeItemResponseDto(makeItemDto(1L), 1L);
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(List.of(makeItem(1L), makeItem(2L)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1L)));
        when(itemRequestRepository.findAllByRequester(any())).thenReturn(List.of(request));

        List<ResponseItemRequestDto> test = itemRequestService.getResponses(1L);

        assertThat(test.size(), equalTo(1));
        assertThat(test.get(0).getItems().size(), equalTo(2));
        assertThat(test.get(0).getItems().contains(responseDto), is(true));
        assertThat(test.get(0).getDescription(), equalTo(response.getDescription()));
    }

    @Test
    void getResponses_shouldReturnEmptyList() {
        when(itemRepository.findAllByRequestId(any())).thenReturn(Collections.emptyList());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1L)));
        when(itemRequestRepository.findAllByRequester(any())).thenReturn(List.of(request));

        assertThat(itemRequestService.getResponses(1L).get(0).getItems(), empty());
    }

    @Test
    void getResponses_shouldReturnUserException() {
        when(userRepository.findById(anyLong())).thenThrow(new NotFoundException("message"));

        assertThatThrownBy(() -> itemRequestService.getResponses(1L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getRequestByOtherUser() {
        when(itemRequestRepository.findAllByRequesterIsNotOrderByTimeCreateDesc(any(), any())).thenReturn(List.of(request));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1L)));

        assertThat(itemRequestService.getRequestByOtherUser(1L, 1, 1).size(), equalTo(1));
    }

    @Test
    void getRequestById_shouldReturnRequest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1L)));
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(List.of(makeItem(1L), makeItem(1L)));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(request));

        ItemResponseDto responseDto = makeItemResponseDto(makeItemDto(1L), 1L);
        assertThat(itemRequestService.getRequestById(1L, 1L).getCreated(), equalTo(response.getCreated()));
        assertThat(itemRequestService.getRequestById(1L, 1L).getDescription(), equalTo(response.getDescription()));
        List<ItemResponseDto> items = itemRequestService.getRequestById(1L, 1L).getItems();
        assertThat(items.size(), equalTo(2));
        assertThat(items.contains(responseDto), is(true));
    }
}