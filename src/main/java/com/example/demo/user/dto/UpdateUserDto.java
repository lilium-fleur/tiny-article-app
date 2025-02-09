package com.example.demo.user.dto;

public record UpdateUserDto(
        String email,
        String username,
        String password
) {
}
