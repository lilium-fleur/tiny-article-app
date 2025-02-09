package com.example.demo.token.refresh;

import com.example.demo.token.shared.TokenMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper extends TokenMapper<RefreshToken> {
}
