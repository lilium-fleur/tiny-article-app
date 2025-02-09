package com.example.demo.content.published.article.dto;


import com.example.demo.content.shared.dto.ArticleDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PublishedDto (
        Long id,
        String title,
        String content,
        Long author,
        LocalDateTime createdAt
) implements ArticleDto{}
