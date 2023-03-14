package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto createBooking(BookingDto booking, Long userId);

    List<BookingResponseDto> getBookingsByUser(Long userId, State state, Pageable pageable);

    BookingResponseDto findBookingById(Long id, Long userId);

    BookingResponseDto change(Long bookingId, Long userId, boolean isApproved);

    List<BookingResponseDto> findBookingsByOwner(Long ownerId, State state, Pageable pageable);
}
