package ru.practicum.shareit.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentResponseDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static Comment mapToComment(CommentDto dto) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        return comment;
    }

    public static CommentResponseDto mapToCommentResponseDto(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setAuthorName(comment.getAuthorName().getName());
        commentResponseDto.setId(comment.getId());
        commentResponseDto.setText(comment.getText());
        commentResponseDto.setCreated(comment.getCreated());
        return commentResponseDto;
    }
}
