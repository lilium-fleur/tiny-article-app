package com.example.demo.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginDto(
        @NotBlank
        String email,
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotBlank
        String fingerprint
) {
}
