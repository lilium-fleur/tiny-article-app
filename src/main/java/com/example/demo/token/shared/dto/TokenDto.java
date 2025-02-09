package com.example.demo.token.shared.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TokenDto(
        Long id,
        String token,
        LocalDateTime expiresAt,
        LocalDateTime createdAt,
        Boolean isRevoked
) {
}
