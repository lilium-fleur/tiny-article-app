package com.example.demo.chat.dto;

import lombok.Builder;

@Builder
public record ChatNotificationDto(
        Long id,
        Long sender,
        Long recipient,
        String content
) {
}
