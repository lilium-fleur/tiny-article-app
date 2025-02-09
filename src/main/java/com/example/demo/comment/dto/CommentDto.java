package com.example.demo.comment.dto;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        String comment,
        Long articleId,
        Long authorId,
        Long replyId,
        LocalDateTime createdAt
) {
}
