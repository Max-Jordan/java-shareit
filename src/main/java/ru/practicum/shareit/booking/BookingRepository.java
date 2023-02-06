package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime start);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, State status);

    List<Booking> findAllByItemIdOwnerOrderByStartDesc(Long ownerId);

    List<Booking> findAllByItemIdOwnerAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime start);

    List<Booking> findAllByItemIdOwnerAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime start);

    List<Booking> findAllByItemIdOwnerAndStartBeforeAndEndAfter(Long ownerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemIdOwnerAndStatusOrderByStartDesc(Long ownerId, State status);

    List<Booking> findAllByItemIdOrderByStartDesc(Long itemId);

    List<Booking> findByItemIdAndBookerIdAndEndIsBeforeOrderByStartDesc(Long itemId, Long userId, LocalDateTime end);
}
