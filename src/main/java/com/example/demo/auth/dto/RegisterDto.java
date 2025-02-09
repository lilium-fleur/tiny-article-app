package com.example.demo.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterDto(
        @Size(min = 6, max = 30)
        @NotBlank
        String email,

        @Size(min = 4, max = 20)
        @NotBlank
        String username,

        @Size(min = 8)
        @NotBlank
        String password,

        @NotBlank
        String fingerprint
) {
}
