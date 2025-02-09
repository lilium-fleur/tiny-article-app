package com.example.demo.content.draft.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateDraftDto(
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}
