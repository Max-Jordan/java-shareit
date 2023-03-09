package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.dto.ShortBooking;
import ru.practicum.shareit.comment.CommentResponseDto;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(name = "item_name")
    private String name;

    @Column
    private String description;
    @Column(name = "is_available")
    private Boolean available;

    @Column(name = "owner_id")
    private Long idOwner;

    @Column(name = "request_id")
    private Long requestId;

    @Transient
    private ShortBooking nextBooking;
    @Transient
    private ShortBooking lastBooking;
    @Transient
    private List<CommentResponseDto> comments;
}
