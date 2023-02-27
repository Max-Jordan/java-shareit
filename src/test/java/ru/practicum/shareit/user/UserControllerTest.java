package ru.practicum.shareit.user;

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
import ru.practicum.shareit.exception.ExceptionController;
import ru.practicum.shareit.exception.NotFoundException;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.factories.UserFactory.*;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController userController;

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private final UserDto dto = makeUserDto(1);

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(ExceptionController.class).build();
    }

    @Test
    void getAllUsers_shouldReturnListUsers() throws Exception {
        List<UserDto> lists = List.of(makeUserDto(1), makeUserDto(2), makeUserDto(3));
        when(service.getAllUsers()).thenReturn(lists);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", equalTo(lists.get(0).getName())))
                .andExpect(jsonPath("$[1].id", equalTo(lists.get(1).getId()), Long.class))
                .andExpect(jsonPath("$[2].email", equalTo(lists.get(2).getEmail())))
                .andExpect(content().json(mapper.writeValueAsString(lists)));
    }

    @Test
    void getAllUsers_shouldReturnEmptyList() throws Exception {
        when(service.getAllUsers()).thenReturn(Collections.emptyList());

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void saveUser_shouldCreateUser() throws Exception {

        when(service.saveUser(any())).thenReturn(dto);

        mvc.perform(post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(UserDto.builder()
                                .name(dto.getName())
                                .email(dto.getEmail()).build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(dto.getId()), Long.class))
                .andExpect(jsonPath("$.name", equalTo(dto.getName())))
                .andExpect(jsonPath("$.email", equalTo(dto.getEmail())));
    }

    @Test
    void saveUser_shouldThrowException() throws Exception {
        mvc.perform(post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(UserDto.builder().name("test").email(null).build())))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        when(service.getUserById(anyLong())).thenReturn(dto);


        mvc.perform(get("/users/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(dto.getId()), Long.class))
                .andExpect(jsonPath("$.name", equalTo(dto.getName())))
                .andExpect(jsonPath("$.email", equalTo(dto.getEmail())));
    }

    @Test
    void getUserById_shouldReturnNotFoundException() throws Exception {
        when(service.getUserById(anyLong())).thenThrow(new NotFoundException("User was not found"));

        mvc.perform(get("/users/{userId}", 90))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateUser_shouldReturnNewUser() throws Exception {
        when(service.editUser(anyLong(), any()))
                .thenReturn(dto);

        mvc.perform(patch("/users/{userId}", dto.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo(dto.getEmail())))
                .andExpect(jsonPath("$.id", equalTo(dto.getId()), Long.class))
                .andExpect(jsonPath("$.name", equalTo(dto.getName())));
    }

    @Test
    void updateUser_shouldReturnException() throws Exception {
        UserDto editUser = UserDto.builder().name("editName").build();
        when(service.editUser(anyLong(), any()))
                .thenThrow(new NotFoundException("User was not found"));

        mvc.perform(patch("/users/{userId}", 12)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(editUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteUser_shouldReturnStatusOk() throws Exception {
        mvc.perform(delete("/users/{userId}", 1))
                .andExpect(status().isOk());
    }
}
