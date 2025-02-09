package com.example.demo.user.dto;

import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String email,
        String username
) {}
