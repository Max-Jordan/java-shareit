package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    private static final String SHARER_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public Booking createBooking(@RequestHeader(SHARER_HEADER) Long userId,
                                            @RequestBody BookingDto booking) {
        return bookingService.createBooking(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking change(@PathVariable Long bookingId,
                                             @RequestParam(value = "approved") boolean isApproved,
                                             @RequestHeader(SHARER_HEADER) Long userId) {
        return bookingService.change(bookingId, userId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public Booking findBookingById(@RequestHeader(SHARER_HEADER) Long userId,@PathVariable Long bookingId) {
        return bookingService.findBookingById(bookingId, userId);
    }

    @GetMapping
    public List<Booking> findBookingsByUser(@RequestHeader(SHARER_HEADER) Long userId,
                                                       @RequestParam(required = false,
                                                               value = "state",
                                                               defaultValue = "ALL") String state) {
        return bookingService.getBookingsByUser(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> findBookingByOwner(@RequestHeader(SHARER_HEADER) Long ownerId,
                                                       @RequestParam(required = false,
                                                               value = "state",
                                                               defaultValue = "ALL") String state) {
        return bookingService.findBookingsByOwner(ownerId, state);
    }
}
