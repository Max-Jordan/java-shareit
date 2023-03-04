package ru.practicum.shareit.factories;

import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class CommentFactory {

    public static Comment makeComment(long id, User user, Item item) {
        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setAuthorName(user);
        comment.setItemId(item);
        comment.setId(id);
        comment.setText("comment");
        return comment;
    }

    public static CommentDto makeCommentDto(Comment comment) {
        return new CommentDto(comment.getText());
    }

    public static CommentResponseDto makeResponseDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setAuthorName(comment.getAuthorName().getName());
        dto.setCreated(comment.getCreated());
        dto.setText(comment.getText());
        dto.setId(comment.getId());
        return dto;
    }
}
