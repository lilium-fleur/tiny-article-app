package com.example.demo.subscription.dto;

import java.time.LocalDateTime;

public record SubscriptionDto(
        Long id,
        Long followerId,
        Long followingId,
        LocalDateTime createdAt
) {
}
