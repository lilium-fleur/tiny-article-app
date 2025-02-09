package com.example.demo.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrUpdateCommentDto(
        @NotBlank
        @Size(min = 1, max = 255)
        String comment
) {
}
