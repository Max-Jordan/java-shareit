package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.CommentResponseDto;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.factories.CommentFactory.*;
import static ru.practicum.shareit.factories.ItemsFactory.*;
import static ru.practicum.shareit.factories.UserFactory.makeUser;
import static ru.practicum.shareit.factories.UserFactory.makeUserDto;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private ItemDto dto;

    private Item item;

    private ItemResponseDto itemResponseDto;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        dto = makeItemDto(1);
        item = makeItem(1);
        itemResponseDto = makeItemResponseDto(dto, 1);
        userDto = makeUserDto(1);
    }

    @Test
    void saveItem_shouldReturnItem() {
        when(userService.getUserById(anyLong())).thenReturn(userDto);
        when(itemRepository.save(any())).thenReturn(item);

        assertThat(itemService.saveItem(userDto.getId(),dto).equals(itemResponseDto), is(true));
    }

    @Test
    void getItemsBySearch_shouldReturnEmptyList() {
        assertThat(itemService.getItemBySearch("",1 ,1), equalTo(Collections.emptyList()));
    }

    @Test
    void getItemsBySearch_shouldReturnList() {
        when(itemRepository.findAllByNameOrDescriptionContainingIgnoreCase(anyString(), anyString(), any()))
                .thenReturn(List.of(makeItem(2), item));

        assertThat(itemService.getItemBySearch("test",2, 2).size(), equalTo(2));
        assertThat(itemService.getItemBySearch("test", 2, 2)
                .contains(itemResponseDto), is(true));
    }
    @Test
    void getItemsByUser_shouldReturnList() {
        when(itemRepository.findAllByIdOwner(anyLong(), any())).thenReturn(List.of(item));
        assertThat(itemService.getItemsByUser(1L, 2, 2).get(0).equals(itemResponseDto),is(true));
    }

    @Test
    void getItemById_shouldReturnItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        itemResponseDto.setComments(Collections.emptyList());

        assertThat(itemService.getItemById(1L,1L).equals(itemResponseDto), is(true));
    }

    @Test void getItemBy_shouldReturnException() {
        when(itemRepository.findById(anyLong())).thenThrow(new NotFoundException("Not found"));
        assertThatThrownBy(() -> itemService.getItemById(1L,1L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void editItem_shouldReturnNewItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ItemDto updateItem = dto;
        updateItem.setDescription("update description");
        itemResponseDto.setDescription("update description");
        item.setDescription(updateItem.getDescription());

        when(itemRepository.saveAndFlush(any())).thenReturn(item);


        assertThat(itemService.editItem(1L,1L, updateItem)
                .equals(itemResponseDto), is(true));
    }

    @Test
    void editItem_shouldReturnException() {
        when(itemRepository.findById(anyLong())).thenThrow(new NotFoundException("item not found"));

        assertThatThrownBy(() -> itemService.editItem(1L,1L,dto)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void editItem_shouldThrowExceptionByOwner() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> itemService.editItem(99L,1L, dto)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void addComment_shouldReturnComment() {
        Comment comment = makeComment(1L,makeUser(1L),item);
        CommentDto commentDto = makeCommentDto(comment);
        CommentResponseDto response = makeResponseDto(comment);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userService.getUserById(anyLong())).thenReturn(userDto);
        when(commentRepository.save(any())).thenReturn(comment);
        when(bookingRepository.findByItemIdAndBookerIdAndEndIsBeforeOrderByStartDesc(anyLong(),anyLong(),any()))
                .thenReturn(List.of(new Booking()));
        assertThat(itemService.addComment(1L,1L, commentDto).getAuthorName(),
                equalTo(response.getAuthorName()));
        assertThat(itemService.addComment(1L,1L,commentDto).getText(), equalTo(response.getText()));
    }

    @Test
    void addComment_shouldReturnException() {
        Comment comment = makeComment(1L,makeUser(1L),item);
        CommentDto commentDto = makeCommentDto(comment);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userService.getUserById(anyLong())).thenReturn(userDto);
        assertThatThrownBy(()-> itemService.addComment(1L,1L,commentDto))
                .isInstanceOf(BookingException.class);
    }
}
