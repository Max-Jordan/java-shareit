package ru.practicum.shareit.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.ShortBooking;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static Booking mapToBooking(BookingDto dto) {
        Booking booking = new Booking();
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        return booking;
    }

    public static ShortBooking mapToShortBooking(Booking booking) {
        ShortBooking shortBooking = new ShortBooking();
        shortBooking.setId(booking.getId());
        shortBooking.setBookerId(booking.getBooker().getId());
        return shortBooking;
    }

    public static BookingResponseDto mapToBookingResponseDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setItem(booking.getItem());
        dto.setBooker(booking.getBooker());
        dto.setStatus(booking.getStatus());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        return dto;
    }
}
