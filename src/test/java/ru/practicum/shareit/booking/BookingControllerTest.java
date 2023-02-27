package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.ExceptionController;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.factories.BookingFactory.*;
import static ru.practicum.shareit.factories.ItemsFactory.makeItem;
import static ru.practicum.shareit.factories.UserFactory.makeUser;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mvc;

    private BookingDto dto;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private BookingResponseDto response;

    private static final String USER_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(bookingController).setControllerAdvice(ExceptionController.class).build();
        Item item = makeItem(1);
        User booker = makeUser(1L);
        dto = makeBookingDto(1);
        response = makeResponseDto(makeBooking(1, item, booker));
    }

    @Test
    void createBooking_shouldReturnBooking() throws Exception {
        when(bookingService.createBooking(any(), anyLong())).thenReturn(response);

        mvc.perform(post("/bookings")
                        .header(USER_HEADER, 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void change() throws Exception {
        response.setStatus(State.APPROVED);
        when(bookingService.change(anyLong(), anyLong(), anyBoolean())).thenReturn(response);

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void findBookingById() throws Exception {
        when(bookingService.findBookingById(anyLong(), anyLong())).thenReturn(response);

        mvc.perform(get("/bookings/{bookingId}", 1)
                        .header(USER_HEADER, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void findBookingsByUser_shouldReturnException() throws Exception {

        mvc.perform(get("/bookings")
                        .param("state", "unsupported")
                        .header(USER_HEADER, 1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void findBookingsByUser() throws Exception {
        when(bookingService.getBookingsByUser(anyLong(), any(), any())).thenReturn(List.of(response));

        mvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .header(USER_HEADER, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(response))));
    }

    @Test
    void findBookingByOwner_shouldThrowStatusException() throws Exception {
        mvc.perform(get("/bookings/owner")
                        .param("state", "unsupported")
                        .header(USER_HEADER, 1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void findBookingByOwner() throws Exception {
        when(bookingService.findBookingsByOwner(anyLong(), any(), any())).thenReturn(List.of(response));

        mvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .header(USER_HEADER, 1))
                .andExpect(content().json(mapper.writeValueAsString(List.of(response))));
    }
}