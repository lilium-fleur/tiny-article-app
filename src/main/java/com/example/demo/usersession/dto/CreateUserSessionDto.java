package com.example.demo.usersession.dto;

import com.example.demo.user.User;
import lombok.Builder;

@Builder
public record CreateUserSessionDto(
        User user
) {
}
