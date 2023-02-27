package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.factories.BookingFactory.*;
import static ru.practicum.shareit.factories.ItemsFactory.makeItem;
import static ru.practicum.shareit.factories.UserFactory.makeUser;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingResponseDto response;

    private BookingDto dto;

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = makeBooking(1, makeItem(1), makeUser(1));
        dto = makeBookingDto(1);
        response = makeResponseDto(booking);
    }

    @Test
    void createBooking_shouldReturnBooking() {
        when(bookingRepository.save(any())).thenReturn(booking);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1)));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(makeItem(1)));

        assertThat(bookingService.createBooking(dto, 2L).getId(), equalTo(response.getId()));
    }

    @Test
    void createBooking_shouldThrowUserException() {
        when(userRepository.findById(anyLong())).thenThrow(new NotFoundException("message"));

        assertThatThrownBy(() -> bookingService.createBooking(dto, 2L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void createBooking_shouldThrowItemNotFound() {

        assertThatThrownBy(() -> bookingService.createBooking(dto, 2L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void createBooking_failedAvailable() {
        Item item = makeItem(2L);
        item.setAvailable(false);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1L)));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.createBooking(dto, 2L)).isInstanceOf(BookingException.class);
    }

    @Test
    void createBooking_startIsBeforeThanNow() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1L)));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(makeItem(2L)));

        dto.setStart(LocalDateTime.now().minusDays(1));
        assertThatThrownBy(() -> bookingService.createBooking(dto, 2L)).isInstanceOf(BookingException.class);
    }

    @Test
    void createBooking_endIsBeforeThanNow() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1L)));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(makeItem(1L)));

        dto.setEnd(LocalDateTime.now().minusDays(1));
        assertThatThrownBy(() -> bookingService.createBooking(dto, 2L)).isInstanceOf(BookingException.class);
    }

    @Test
    void createBooking_endIsBeforeThanStart() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1L)));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(makeItem(1L)));

        dto.setEnd(dto.getStart().minusDays(1));
        assertThatThrownBy(() -> bookingService.createBooking(dto, 1L)).isInstanceOf(BookingException.class);
    }

    @Test
    void createBooking_itemOwnerEqualBookerId() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(makeItem(1L)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1L)));

        assertThatThrownBy(() -> bookingService.createBooking(dto, 1L)).isInstanceOf(NotFoundException.class);
    }


    @Test
    void findBookingById() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThat(bookingService.findBookingById(1L, 1L).getId(), equalTo(response.getId()));
        assertThat(bookingService.findBookingById(1L, 1L).getStatus(), equalTo(response.getStatus()));
        assertThat(bookingService.findBookingById(1L, 1L).getItem(), equalTo(response.getItem()));
        assertThat(bookingService.findBookingById(1L, 1L).getBooker(), equalTo(response.getBooker()));
        assertThat(bookingService.findBookingById(1L, 1L).getStart(), equalTo(response.getStart()));
        assertThat(bookingService.findBookingById(1L, 1L).getEnd(), equalTo(response.getEnd()));
    }

    @Test
    void findBookingById_shouldReturnException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.findBookingById(1L, 99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void changeBooking_shouldReturnApprovedBooking() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        Booking responseBooking = makeBooking(2, booking.getItem(), booking.getBooker());
        responseBooking.setStatus(State.APPROVED);
        when(bookingRepository.saveAndFlush(any())).thenReturn(responseBooking);

        assertThat(bookingService.change(1L, 1L, true).getStatus(), equalTo(State.APPROVED));
    }

    @Test
    void changeBooking_shouldReturnRejectedBooking() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        Booking responseBooking = makeBooking(2, booking.getItem(), booking.getBooker());
        responseBooking.setStatus(State.REJECTED);
        when(bookingRepository.saveAndFlush(any())).thenReturn(responseBooking);

        assertThat(bookingService.change(1L, 1L, false).getStatus(), equalTo(State.REJECTED));
    }

    @Test
    void changeBooking_stateIsApprovedException() {
        booking.setStatus(State.APPROVED);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.change(1L, 1L, true)).isInstanceOf(BookingException.class);
    }

    @Test
    void changeBooking_userNotOwner() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.change(1L, 99L, true)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getAllByBooker_shouldCallFindAllByBookerIdOrderByStartDesc() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1)));
        bookingService.getBookingsByUser(1L, State.ALL, null);
        verify(bookingRepository, atMostOnce()).findAllByBookerIdOrderByStartDesc(anyLong(), any());
    }


    @Test
    void getAllByBooker_shouldCallFindAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1)));
        bookingService.getBookingsByUser(1L, State.CURRENT, null);
        verify(bookingRepository, atMostOnce())
                .findAllByBookerIdAndStartBeforeAndEndAfter(Mockito.anyLong(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void getAllByBooker_shouldCallFindAllByBookerIdAndEndBeforeOrderByStartDesc() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1)));
        bookingService.getBookingsByUser(1L, State.PAST, null);
        verify(bookingRepository, atMostOnce())
                .findAllByBookerIdAndEndBeforeOrderByStartDesc(Mockito.anyLong(), Mockito.any(), Mockito.any());
    }

    @Test
    void getAllByBooker_shouldCallFindAllByBookerIdAndStartAfterOrderByStartDesc() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1)));
        bookingService.getBookingsByUser(1L, State.FUTURE, null);
        verify(bookingRepository, atMostOnce())
                .findAllByBookerIdAndStartAfterOrderByStartDesc(Mockito.anyLong(), Mockito.any(), Mockito.any());
    }

    @Test
    void getAllByBooker_shouldCallFindAllByBookerIdAndStatusOrderByStartDescWithStatusWaiting() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1)));
        bookingService.getBookingsByUser(1L, State.WAITING, null);
        verify(bookingRepository, atMostOnce())
                .findAllByBookerIdAndStatusOrderByStartDesc(1L, State.WAITING, null);
    }

    @Test
    void getAllByBooker_shouldCallFindAllByBookerIdAndStatusOrderByStartDescWithStatusRejected() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1)));
        bookingService.getBookingsByUser(1L, State.REJECTED, null);
        verify(bookingRepository, atMostOnce())
                .findAllByBookerIdAndStatusOrderByStartDesc(1L, State.REJECTED, null);
    }

    @Test
    void getAllByOwner_shouldCallFindAllByItemOwnerIdOrderByStartDesc() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1)));
        bookingService.findBookingsByOwner(1L, State.ALL, null);
        verify(bookingRepository, atMostOnce()).findAllByItemIdOwnerOrderByStartDesc(Mockito.anyLong(), Mockito.any());
    }

    @Test
    void getAllByOwner_shouldCallFindAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1)));
        bookingService.findBookingsByOwner(1L, State.CURRENT, null);
        verify(bookingRepository, atMostOnce())
                .findAllByItemIdOwnerAndStartBeforeAndEndAfter(Mockito.anyLong(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void getAllByOwner_shouldCallFindAllByItemOwnerIdAndEndBeforeOrderByStartDesc() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1)));
        bookingService.findBookingsByOwner(1L, State.PAST, null);
        verify(bookingRepository, atMostOnce())
                .findAllByItemIdOwnerAndEndBeforeOrderByStartDesc(Mockito.anyLong(), Mockito.any(), Mockito.any());
    }

    @Test
    void getAllByOwner_shouldCallFindAllByItemOwnerIdAndStartAfterOrderByStartDesc() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1)));
        bookingService.findBookingsByOwner(1L, State.FUTURE, null);
        verify(bookingRepository, atMostOnce())
                .findAllByItemIdOwnerAndStartAfterOrderByStartDesc(Mockito.anyLong(), Mockito.any(), Mockito.any());
    }

    @Test
    void getAllByOwner_shouldCallFindAllByItemOwnerIdAndStatusOrderByStartDescWithStatusWaiting() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1)));
        bookingService.findBookingsByOwner(1L, State.WAITING, null);
        verify(bookingRepository, atMostOnce())
                .findAllByItemIdOwnerAndStatusOrderByStartDesc(1L, State.WAITING, null);
    }

    @Test
    void getAllByOwner_shouldCallFindAllByItemOwnerIdAndStatusOrderByStartDescWithStatusRejected() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(makeUser(1)));
        bookingService.findBookingsByOwner(1L, State.REJECTED, null);
        verify(bookingRepository, atMostOnce())
                .findAllByItemIdOwnerAndStatusOrderByStartDesc(1L, State.REJECTED, null);
    }
}