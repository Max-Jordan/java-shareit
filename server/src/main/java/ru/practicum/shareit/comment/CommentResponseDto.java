package ru.practicum.shareit.comment;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
