package com.example.demo.chat.message.dto;

import lombok.Builder;

@Builder
public record DeletedMessageDto(
        Long id,
        Long chat,
        boolean deleted
) {
}
