package ru.practicum.shareit.comment;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User authorName;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item itemId;
    @Column(name = "comment_text")
    @NotBlank
    @Size(max = 1000)
    private String text;
    private LocalDateTime created;
}
