package com.example.demo.__shared.exception;

import lombok.Builder;

@Builder
public record ErrorDto(
        int statusCode,
        String massage,
        String description
) {}
