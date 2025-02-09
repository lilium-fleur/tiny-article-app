package com.example.demo.token.shared.dto;

import com.example.demo.user.User;
import lombok.Builder;
import com.example.demo.usersession.UserSession;

@Builder
public record CreateTokenDto(
        UserSession userSession,
        User user,
        String token
) {
}
