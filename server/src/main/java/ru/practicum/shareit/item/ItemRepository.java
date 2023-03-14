package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT item FROM Item item " +
            "WHERE item.available = TRUE " +
            "AND (UPPER(item.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(item.description) LIKE UPPER(CONCAT('%', ?2, '%')))")
    Page<Item> findAllByNameOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

    List<Item> findAllByRequestId(Long requestId);

    @Query(value = "select * from items " +
            "where owner_id = ?1 " +
            "order by item_id", nativeQuery = true)
    Page<Item> findAllByIdOwner(long ownerId, Pageable pageable);
}
