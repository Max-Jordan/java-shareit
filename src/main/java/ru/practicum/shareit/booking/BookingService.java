package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    Booking createBooking(BookingDto booking, Long userId);

    List<Booking> getBookingsByUser(Long userId, String state);

    Booking findBookingById(Long id, Long userId);

    Booking change(Long bookingId, Long userId, boolean isApproved);

    List<Booking> findBookingsByOwner(Long ownerId, String state);
}
