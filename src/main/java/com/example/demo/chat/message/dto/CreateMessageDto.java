package com.example.demo.chat.message.dto;

import jakarta.validation.constraints.NotBlank;


public record CreateMessageDto(
        @NotBlank
        String content
) {
}
