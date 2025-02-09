package com.example.demo.content.published.article.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreatePublishedDto(
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}
