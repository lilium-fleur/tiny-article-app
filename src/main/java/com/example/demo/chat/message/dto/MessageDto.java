package com.example.demo.chat.message.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageDto(
        Long id,
        String content,
        Long sender,
        Long recipient,
        LocalDateTime createdAt,
        Long chat
) {
}
