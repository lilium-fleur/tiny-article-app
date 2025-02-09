package com.example.demo.reaction.like.dto;

import lombok.Builder;

@Builder
public record LikeDto(
        Long id,
        Long userId,
        Long articleId
) {
}
