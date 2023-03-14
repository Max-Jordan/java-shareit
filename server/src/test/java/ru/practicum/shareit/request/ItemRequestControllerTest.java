package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exception.ExceptionController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static ru.practicum.shareit.factories.ItemRequestFactory.*;
import static ru.practicum.shareit.factories.UserFactory.makeUser;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    private static final String HEADER = "X-Sharer-User-Id";

    @Mock
    private ItemRequestServiceImpl service;

    @InjectMocks
    private ItemRequestController controller;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private ItemRequestDto dto;

    private ResponseItemRequestDto response;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(ExceptionController.class).build();
        dto = makeItemRequestDto(1);
        response = makeResponseItemRequestDto(makeItemRequest(1, makeUser(1L)));
    }


    @Test
    void addItemRequest() throws Exception {
        when(service.save(any(), anyLong())).thenReturn(response);

        mvc.perform(post("/requests")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void getResponsesByRequest() throws Exception {
        List<ResponseItemRequestDto> responses = List.of(response);
        when(service.getResponses(anyLong())).thenReturn(responses);

        mvc.perform(get("/requests")
                        .header(HEADER, 1))
                .andExpect(content().json(mapper.writeValueAsString(responses)));
    }

    @Test
    void getRequestsByOtherUser() throws Exception {
        List<ResponseItemRequestDto> responses = List.of(response);
        when(service.getRequestByOtherUser(anyLong(), anyInt(), anyInt())).thenReturn(responses);

        mvc.perform(get("/requests/all")
                        .header(HEADER, 1)
                        .param("from","0")
                        .param("size", "10"))
                .andExpect(content().json(mapper.writeValueAsString(responses)));
    }

    @Test
    void getRequestById() throws Exception {
        when(service.getRequestById(anyLong(), anyLong())).thenReturn(response);

        mvc.perform(get("/requests/{requestId}", 1)
                        .header(HEADER, 1))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }
}