package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.StatusException;
import ru.practicum.shareit.item.ItemRepository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public Booking createBooking(BookingDto bookingDto, Long userId) {
        User user = userService.getUserById(userId);
        Item item = findItem(bookingDto.getItemId());
        checkBooking(bookingDto, userId);
        Booking booking = BookingMapper.mapToBooking(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(IsApproved.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking findBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("The booking with id " + bookingId + " not exist"));
        if (!(Objects.equals(booking.getItem().getIdOwner(), userId) ||
                Objects.equals(booking.getBooker().getId(), userId))) {
            throw new NotFoundException("Wrong user");
        }
        return booking;
    }

    @Override
    public Booking change(Long bookingId, Long userId, boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("The booking not found"));
        if (!Objects.equals(userId, booking.getItem().getIdOwner())) {
            throw new NotFoundException("Only the owner of the item can confirm the booking");
        }
        if (booking.getStatus().equals(IsApproved.APPROVED)) {
            throw new BookingException("The booking already approved");
        }
        if (isApproved) {
            booking.setStatus(IsApproved.APPROVED);
        }
        if (!isApproved) {
            booking.setStatus(IsApproved.REJECTED);
        }
        return bookingRepository.saveAndFlush(booking);
    }

    @Override
    public List<Booking> getBookingsByUser(Long userId, String state) {
        userService.getUserById(userId);
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            case "FUTURE":
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
            case "PAST":
                return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
            case "CURRENT":
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(
                        userId, LocalDateTime.now(), LocalDateTime.now());
            case "WAITING":
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, IsApproved.WAITING);
            case "REJECTED":
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, IsApproved.REJECTED);
            default:
                throw new StatusException();
        }
    }

    @Override
    public List<Booking> findBookingsByOwner(Long userId, String state) {
        userService.getUserById(userId);
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByItemIdOwnerOrderByStartDesc(userId);
            case "FUTURE":
                return bookingRepository.findAllByItemIdOwnerAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
            case "PAST":
                return bookingRepository.findAllByItemIdOwnerAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
            case "CURRENT":
                return bookingRepository.findAllByItemIdOwnerAndStartBeforeAndEndAfter(
                        userId, LocalDateTime.now(), LocalDateTime.now());
            case "WAITING":
                return bookingRepository.findAllByItemIdOwnerAndStatusOrderByStartDesc(userId, IsApproved.WAITING);
            case "REJECTED":
                return bookingRepository.findAllByItemIdOwnerAndStatusOrderByStartDesc(userId, IsApproved.REJECTED);
            default:
                throw new StatusException();
        }
    }

    private void checkBooking(BookingDto booking, Long userId) {
        Item item = findItem(booking.getItemId());
        if (Boolean.FALSE.equals(item.getAvailable())) {
            throw new BookingException("This item don't available");
        }
        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingException("The booking start time should not be earlier than the current time");
        }
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new BookingException("The booking end time should not be earlier" +
                    " than the current time or start time");
        }
        if (Objects.equals(item.getIdOwner(), userId)) {
            throw new NotFoundException("User with id " + userId + "can't book thing with id " + booking.getItemId());
        }
    }

    private Item findItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("The item with id " + itemId + " not found"));
    }
}
