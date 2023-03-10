package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exception.ExceptionController;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    private static final String X_SHARER_HEADER = "X-Sharer-User-Id";

    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private MockMvc mvc;

    @BeforeEach
    public void setUop() {
        mvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(ExceptionController.class)
                .build();
    }

    @Test
    void saveItem_emptyName() throws Exception {
        mvc.perform(post("/items")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_HEADER, 1)
                        .content(mapper.writeValueAsString(new ItemDto("", "desc", true,
                                1L)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveItem_emptyDescription() throws Exception {
        mvc.perform(post("/items")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_HEADER, 1)
                        .content(mapper.writeValueAsString(new ItemDto("name", "", true,
                                1L)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveItem_negativeRequestId() throws Exception {
        mvc.perform(post("/items")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_HEADER, 1)
                        .content(mapper.writeValueAsString(new ItemDto("name", "desc", true,
                                -1L)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}