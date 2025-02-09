package com.example.demo.token.shared;

import com.example.demo.token.shared.dto.TokenDto;
import org.springframework.stereotype.Component;

@Component
public interface TokenMapper<T extends TokenEntity> {

    TokenDto toDto(T token);
}
