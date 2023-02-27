package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.StatusException;
import ru.practicum.shareit.item.ItemRepository;

import static ru.practicum.shareit.mapper.BookingMapper.mapToBookingResponseDto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingResponseDto createBooking(BookingDto bookingDto, Long userId) {
        User user = findUser(userId);
        Item item = findItem(bookingDto.getItemId());
        checkBooking(bookingDto, userId);
        Booking booking = BookingMapper.mapToBooking(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(State.WAITING);
        return mapToBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto findBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("The booking with id " + bookingId + " not exist"));
        if (!(Objects.equals(booking.getItem().getIdOwner(), userId) ||
                Objects.equals(booking.getBooker().getId(), userId))) {
            throw new NotFoundException("Wrong user");
        }
        return mapToBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto change(Long bookingId, Long userId, boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("The booking not found"));
        if (!Objects.equals(userId, booking.getItem().getIdOwner())) {
            throw new NotFoundException("Only the owner of the item can confirm the booking");
        }
        if (booking.getStatus().equals(State.APPROVED)) {
            throw new BookingException("The booking already approved");
        }
        if (isApproved) {
            booking.setStatus(State.APPROVED);
        } else {
            booking.setStatus(State.REJECTED);
        }
        return mapToBookingResponseDto(bookingRepository.saveAndFlush(booking));
    }

    @Override
    public List<BookingResponseDto> getBookingsByUser(Long userId, State state, Pageable pageable) {
        findUser(userId);
        switch (state) {
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId, pageable).stream()
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(
                                userId, LocalDateTime.now(), LocalDateTime.now(), pageable).stream()
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, State.WAITING, pageable)
                        .stream()
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, State.REJECTED, pageable)
                        .stream()
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            default:
                throw new StatusException();
        }
    }

    @Override
    public List<BookingResponseDto> findBookingsByOwner(Long userId, State state, Pageable pageable) {
        findUser(userId);
        switch (state) {
            case ALL:
                return bookingRepository.findAllByItemIdOwnerOrderByStartDesc(userId, pageable).stream()
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllByItemIdOwnerAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findAllByItemIdOwnerAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(),
                                pageable)
                        .stream()
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllByItemIdOwnerAndStartBeforeAndEndAfter(
                                userId, LocalDateTime.now(), LocalDateTime.now(), pageable).stream()
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllByItemIdOwnerAndStatusOrderByStartDesc(userId, State.WAITING, pageable)
                        .stream()
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findAllByItemIdOwnerAndStatusOrderByStartDesc(userId, State.REJECTED, pageable)
                        .stream()
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            default:
                throw new StatusException();
        }
    }

    private void checkBooking(BookingDto booking, Long userId) {
        Item item = findItem(booking.getItemId());
        if (BooleanUtils.isFalse(item.getAvailable())) {
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
            throw new NotFoundException("User with id " + userId + " can't book thing with id " + booking.getItemId());
        }
    }

    private Item findItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("The item with id " + itemId + " not found"));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("The user with id " + userId + " not found"));
    }
}
