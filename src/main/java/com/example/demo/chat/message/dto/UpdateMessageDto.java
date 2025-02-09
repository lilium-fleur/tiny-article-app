package com.example.demo.chat.message.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateMessageDto(
        @NotBlank
        String content
) {
}
