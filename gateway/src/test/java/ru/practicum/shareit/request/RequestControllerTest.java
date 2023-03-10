package ru.practicum.shareit.request;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {

    private static final String HEADER = "X-Sharer-User-Id";
    @InjectMocks
    private RequestController controller;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private MockMvc mvc;

    @BeforeEach
    public void setUop() {
        mvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(ExceptionController.class)
                .build();
    }

    @Test
    void addItemRequest() throws Exception {
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(new RequestDto("")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER, 1))
                .andExpect(status().is4xxClientError());
    }
}