package ru.practicum.shareit.item;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentResponseDto;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.ExceptionController;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.factories.ItemsFactory.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService service;

    @InjectMocks
    private ItemController controller;
    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final ItemDto dto = makeItemDto(1);

    private final ItemResponseDto responseDto = makeItemResponseDto(dto, 1);

    private static final String SHARER_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(ExceptionController.class).build();
    }

    @Test
    void saveItem_shouldReturnItem() throws Exception {
        when(service.saveItem(anyLong(), any())).thenReturn(responseDto);

        mvc.perform(post("/items")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(content().json(mapper.writeValueAsString(responseDto)));
    }

    @Test
    void saveItem_shouldReturnUserNotFound() throws Exception {
        when(service.saveItem(anyLong(), any())).thenThrow(new NotFoundException("The user was not found"));

        mvc.perform(post("/items")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void editItem_shouldReturnNewItem() throws Exception {
        when(service.editItem(anyLong(), anyLong(), any())).thenReturn(responseDto);

        mvc.perform(patch("/items/{itemId}", 1)
                        .header(SHARER_HEADER, 2)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(responseDto)));
    }

    @Test
    void editItem_shouldReturnException() throws Exception {
        when(service.editItem(anyLong(), anyLong(), any())).thenThrow(new NotFoundException("not found"));

        mvc.perform(patch("/items/{itemId}", 2)
                        .header(SHARER_HEADER, 11)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getItemById_ShouldReturnItem() throws Exception {
        when(service.getItemById(anyLong(), anyLong())).thenReturn(responseDto);

        mvc.perform(get("/items/{itemId}", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(SHARER_HEADER, 1))
                .andExpect(content().json(mapper.writeValueAsString(responseDto)));
    }

    @Test
    void getItemById_shouldReturnNotFoundException() throws Exception {
        when(service.getItemById(anyLong(), anyLong())).thenThrow(new NotFoundException("This item was not found"));

        mvc.perform(get("/items/{itemId}", 99)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_HEADER, 1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getItemsBySearch_shouldReturnListItems() throws Exception {
        List<ItemResponseDto> list = List.of(responseDto,
                makeItemResponseDto(makeItemDto(2), 2),
                makeItemResponseDto(makeItemDto(3), 3));

        when(service.getItemBySearch(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(
                List.of(responseDto,
                        makeItemResponseDto(makeItemDto(2), 2),
                        makeItemResponseDto(makeItemDto(3), 3)));

        mvc.perform(get("/items/search")
                        .param("text", "test")
                        .header(SHARER_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(list)));
    }

    @Test
    void getItemsBySearch_shouldReturnEmptyList() throws Exception {
        when(service.getItemBySearch(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        mvc.perform(get("/items/search")
                        .param("text", "example")
                        .header(SHARER_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(0)));
    }

    @Test
    void publicCommentToItem_shouldReturnItemWithComment() throws Exception {
        CommentResponseDto comment = new CommentResponseDto();
        comment.setCreated(LocalDateTime.now());
        comment.setText("comment");
        comment.setAuthorName("author");
        comment.setId(1L);
        when(service.addComment(anyLong(), anyLong(), any())).thenReturn(comment);

        mvc.perform(post("/items/{itemId}/comment", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SHARER_HEADER, 1)
                        .content(mapper.writeValueAsString(new CommentDto("comment")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(comment)));
    }

    @Test
    void publicCommentToItem_shouldReturnBookingException() throws Exception {
        when(service.addComment(anyLong(), anyLong(), any())).thenThrow(
                new BookingException("This user didn't book the item"));

        mvc.perform(post("/items/{itemId}/comment", 1)
                        .header(SHARER_HEADER, 99)
                        .content(mapper.writeValueAsString(new CommentDto("comment")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getItemsByUser_shouldReturnItems() throws Exception {
        when(service.getItemsByUser(anyLong(), anyInt(), anyInt())).thenReturn(List.of(responseDto));

        mvc.perform(get("/items")
                        .header(SHARER_HEADER, 1))
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andExpect(content().json(mapper.writeValueAsString(List.of(responseDto))));
    }
}
