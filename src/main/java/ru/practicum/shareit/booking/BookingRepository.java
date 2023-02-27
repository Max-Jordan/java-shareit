package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long userId, LocalDateTime start, LocalDateTime end,
                                                             Pageable pageable);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, State status, Pageable pageable);

    List<Booking> findAllByItemIdOwnerOrderByStartDesc(Long ownerId, Pageable pageable);

    List<Booking> findAllByItemIdOwnerAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByItemIdOwnerAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime start,
                                                                    Pageable pageable);

    List<Booking> findAllByItemIdOwnerAndStartBeforeAndEndAfter(Long ownerId, LocalDateTime start, LocalDateTime end,
                                                                Pageable pageable);

    List<Booking> findAllByItemIdOwnerAndStatusOrderByStartDesc(Long ownerId, State status, Pageable pageable);

    List<Booking> findAllByItemIdOrderByStartDesc(Long itemId);

    List<Booking> findByItemIdAndBookerIdAndEndIsBeforeOrderByStartDesc(Long itemId, Long userId, LocalDateTime end);
}
