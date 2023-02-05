package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.ShortBooking;
import ru.practicum.shareit.comment.CommentResponseDto;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column
    private String name;

    @Column
    private String description;
    @Column(name = "is_available")
    private Boolean available;

    @Column(name = "owner_id")
    private Long idOwner;

    @Transient
    private ShortBooking nextBooking;
    @Transient
    private ShortBooking lastBooking;
    @Transient
    private List<CommentResponseDto> comments;
}
