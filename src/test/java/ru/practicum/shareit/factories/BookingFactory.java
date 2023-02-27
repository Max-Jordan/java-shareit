package ru.practicum.shareit.factories;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingFactory {

    public static Booking makeBooking(long id, Item item, User user) {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.MIN);
        booking.setEnd(LocalDateTime.MAX);
        booking.setId(id);
        booking.setStatus(State.WAITING);
        booking.setItem(item);
        booking.setBooker(user);
        return booking;
    }

    public static BookingDto makeBookingDto(long id) {
        BookingDto dto = new BookingDto();
        dto.setItemId(id);
        dto.setStart(LocalDateTime.now().plusMinutes(10));
        dto.setEnd(LocalDateTime.now().plusDays(1));
        return dto;
    }

    public static BookingResponseDto makeResponseDto(Booking booking) {
        BookingResponseDto response = new BookingResponseDto();
        response.setItem(booking.getItem());
        response.setEnd(booking.getEnd());
        response.setStart(booking.getStart());
        response.setBooker(booking.getBooker());
        response.setId(booking.getId());
        response.setStatus(State.WAITING);
        return response;
    }
}
