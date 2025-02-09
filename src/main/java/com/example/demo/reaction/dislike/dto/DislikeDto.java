package com.example.demo.reaction.dislike.dto;

import lombok.Builder;

@Builder
public record DislikeDto(
        Long id,
        Long userId,
        Long articleId
){
}
