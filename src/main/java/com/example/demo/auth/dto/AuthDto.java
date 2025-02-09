package com.example.demo.auth.dto;


import com.example.demo.token.shared.dto.TokenDto;
import com.example.demo.user.dto.UserDto;
import lombok.Builder;

@Builder
public record AuthDto(
        UserDto userDto,
        TokenDto token
){
}
