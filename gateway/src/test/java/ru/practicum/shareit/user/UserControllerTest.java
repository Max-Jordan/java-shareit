package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exception.ExceptionController;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final String HEADER = "X-Sharer-User-Id";
    @InjectMocks
    private UserController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(ExceptionController.class)
                .build();
    }


    @Test
    void saveUser_emptyName() throws Exception {
        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(new UserDto("", "mail@mail.com")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveUser_incorrectEmail() throws Exception {
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(new UserDto("name", "mailmail.com")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void editUser_emptyName() throws Exception {
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(new UserDto("", "mail@mail.com")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void editUser_incorrectEmail() throws Exception {
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(new UserDto("name", "mailmail.com")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}