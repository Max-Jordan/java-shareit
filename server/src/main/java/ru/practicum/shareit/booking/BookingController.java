package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.StatusException;
import ru.practicum.shareit.mapper.PaginationMapper;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;

    private static final String SHARER_HEADER = "X-Sharer-User-Id";
    private static final String MESSAGE_FOR_INDEX = "The index can't be negative";
    private static final String MESSAGE_FOR_SIZE = "The size can't be less than one";

    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader(SHARER_HEADER) Long userId,
                                            @RequestBody BookingDto booking) {
        return bookingService.createBooking(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto change(@PathVariable Long bookingId,
                                     @RequestParam(value = "approved") boolean isApproved,
                                     @RequestHeader(SHARER_HEADER) Long userId) {
        return bookingService.change(bookingId, userId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto findBookingById(@RequestHeader(SHARER_HEADER) Long userId, @PathVariable Long bookingId) {
        return bookingService.findBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> findBookingsByUser(@RequestHeader(SHARER_HEADER) Long userId,
                                                       @RequestParam(required = false,
                                                               value = "state", defaultValue = "ALL") String state,
                                                       @RequestParam(name = "from",
                                                               required = false, defaultValue = "0")
                                                       @Min(value = 0, message = MESSAGE_FOR_INDEX) Integer index,
                                                       @RequestParam(name = "size",
                                                               required = false, defaultValue = "20")
                                                       @Min(value = 1, message = MESSAGE_FOR_SIZE) Integer size) {
        try {
            return bookingService.getBookingsByUser(userId, Enum.valueOf(State.class, state),
                    PaginationMapper.mapToPageable(index, size));
        } catch (IllegalArgumentException e) {
            throw new StatusException();
        }
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> findBookingByOwner(@RequestHeader(SHARER_HEADER) Long ownerId,
                                                       @RequestParam(required = false,
                                                               value = "state",
                                                               defaultValue = "ALL") String state,
                                                       @RequestParam(name = "from", required = false, defaultValue = "0")
                                                       @Min(value = 0, message = MESSAGE_FOR_INDEX) Integer index,
                                                       @RequestParam(name = "size", required = false, defaultValue = "20")
                                                       @Min(value = 1, message = MESSAGE_FOR_SIZE) Integer size) {
        try {
            return bookingService.findBookingsByOwner(ownerId, Enum.valueOf(State.class, state),
                    PaginationMapper.mapToPageable(index, size));
        } catch (IllegalArgumentException e) {
            throw new StatusException();
        }
    }
}
