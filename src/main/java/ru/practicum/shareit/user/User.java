package ru.practicum.shareit.user;

import lombok.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.*;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    @NotEmpty
    @Email(message = "Please enter the correct email")
    private String email;
    private String name;

    private final Map<Long, Item> items = new HashMap<>();

    public void addItem(Item item) {
        items.put(item.getId(), item);
    }

    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }

    public void editItem(Item item) {
        items.replace(item.getId(), item);
    }
}
