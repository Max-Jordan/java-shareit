package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByNameOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

    List<Item> findAllByRequestId(Long requestId);

    Page<Item> findAllByIdOwner(long ownerId, Pageable pageable);
}
