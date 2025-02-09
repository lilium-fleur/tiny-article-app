package com.example.demo.token.access;

import com.example.demo.token.shared.TokenMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccessTokenMapper extends TokenMapper<AccessToken> {

}
