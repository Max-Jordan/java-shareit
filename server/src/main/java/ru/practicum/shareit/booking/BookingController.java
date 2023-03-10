package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.StatusException;
import ru.practicum.shareit.mapper.PaginationMapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    private static final String SHARER_HEADER = "X-Sharer-User-Id";

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
                                                       @RequestParam(value = "state") String state,
                                                       @RequestParam(name = "from") Integer index,
                                                       @RequestParam(name = "size") Integer size) {
        try {
            return bookingService.getBookingsByUser(userId, Enum.valueOf(State.class, state),
                    PaginationMapper.mapToPageable(index, size));
        } catch (IllegalArgumentException e) {
            throw new StatusException();
        }
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> findBookingByOwner(@RequestHeader(SHARER_HEADER) Long ownerId,
                                                       @RequestParam(value = "state") String state,
                                                       @RequestParam(name = "from") Integer index,
                                                       @RequestParam(name = "size") Integer size) {
        try {
            return bookingService.findBookingsByOwner(ownerId, Enum.valueOf(State.class, state),
                    PaginationMapper.mapToPageable(index, size));
        } catch (IllegalArgumentException e) {
            throw new StatusException();
        }
    }
}
