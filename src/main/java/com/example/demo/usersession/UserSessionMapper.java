package com.example.demo.usersession;

import com.example.demo.usersession.dto.UserSessionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserSessionMapper {

    UserSessionDto toDto(UserSession userSession);

}
