package com.example.demo.chat.message.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record MessageResponse(
        List<MessageDto> messages,
        Long nextCursor,
        boolean hasMore
) {
}
