package com.example.demo.content.published.reposted.dto;

import com.example.demo.content.shared.dto.ArticleDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RepostedDto(
        Long id,
        Long original,
        Long author,
        String comment,
        LocalDateTime createdAt
) implements ArticleDto {}
