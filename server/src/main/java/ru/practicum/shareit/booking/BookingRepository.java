package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long userId, LocalDateTime start, LocalDateTime end,
                                                             Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, State status, Pageable pageable);

    Page<Booking> findAllByItemIdOwnerOrderByStartDesc(Long ownerId, Pageable pageable);

    Page<Booking> findAllByItemIdOwnerAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByItemIdOwnerAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime start,
                                                                    Pageable pageable);

    Page<Booking> findAllByItemIdOwnerAndStartBeforeAndEndAfter(Long ownerId, LocalDateTime start, LocalDateTime end,
                                                                Pageable pageable);

    Page<Booking> findAllByItemIdOwnerAndStatusOrderByStartDesc(Long ownerId, State status, Pageable pageable);

    List<Booking> findAllByItemIdOrderByStartDesc(Long itemId);

    List<Booking> findByItemIdAndBookerIdAndEndIsBeforeOrderByStartDesc(Long itemId, Long userId, LocalDateTime end);
}
