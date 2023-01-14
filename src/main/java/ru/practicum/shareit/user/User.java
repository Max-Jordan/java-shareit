package ru.practicum.shareit.user;

import lombok.*;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    @NotEmpty
    @Email(message = "Please enter the correct email")
    private String email;
    private String name;
    private Map<Long, Item> items = new HashMap<>();

    public Map<Long, Item> getItems() {
        return items;
    }

    public void setItems(Map<Long, Item> items) {
        this.items = items;
    }
}
