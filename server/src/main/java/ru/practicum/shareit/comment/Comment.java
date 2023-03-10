package ru.practicum.shareit.comment;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User authorName;
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item itemId;
    @Column(name = "comment_text", nullable = false, length = 1000)
    private String text;
    private LocalDateTime created;
}
