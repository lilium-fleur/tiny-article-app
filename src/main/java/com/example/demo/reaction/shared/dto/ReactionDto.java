package com.example.demo.reaction.shared.dto;

import com.example.demo.reaction.dislike.dto.DislikeDto;
import com.example.demo.reaction.like.dto.LikeDto;
import lombok.Builder;

import java.util.List;

@Builder
public record ReactionDto(
        Long articleId,
        List<LikeDto> Likes,
        List<DislikeDto> Dislikes
) {
}
