package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.exception.ExceptionController;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private static final String HEADER = "X-Sharer-User-Id";
    private static final String STATE = "state";
    private static final String FROM = "from";
    private static final String SIZE = "size";

    @InjectMocks
    private BookingController bookingController;
    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(bookingController).setControllerAdvice(ExceptionController.class)
                .build();
    }


    @Test
    void getBookings_shouldReturnServerError() throws Exception {
        mvc.perform(get("/bookings")
                        .header(HEADER, 1)
                        .param(FROM, "0")
                        .param(SIZE, "10")
                        .param(STATE, "..."))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getBookings_shouldReturnBadRequestError() throws Exception {
        mvc.perform(get("/bookings")
                        .param(FROM, "1")
                        .param(SIZE, "1")
                        .param(STATE, "all"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void bookItem_shouldReturnValidateException() throws Exception {
        mvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HEADER, 1)
                .content(mapper.writeValueAsString(new BookItemRequestDto(1,
                        LocalDateTime.of(2000, 10, 10, 10, 10, 10),
                        LocalDateTime.now()))))
                .andExpect(status().is4xxClientError());
    }
    @Test
    void getBookingsByOwner() throws Exception{
        mvc.perform(get("/bookings/owner")
                .param(FROM, "1")
                .param(SIZE, "1"))
                .andExpect(status().is4xxClientError());
    }
}