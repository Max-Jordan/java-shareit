package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.List;

@Component
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequester(User requester);

    Page<ItemRequest> findItemRequestByRequesterIsNotOrderByTimeCreateDesc(User requester, Pageable pageable);
}
