package com.example.demo.usersession.dto;

import java.time.LocalDateTime;

public record UserSessionDto(
        Long id,
        String ipAddress,
        String deviceInfo,
        LocalDateTime lastActivity,
        Boolean isActive
) {
}
