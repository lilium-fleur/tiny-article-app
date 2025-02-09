package com.example.demo.chat;

import com.example.demo.chat.dto.ChatDto;
import com.example.demo.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(target = "user1", source = "user1", qualifiedByName = "toId")
    @Mapping(target = "user2", source = "user2", qualifiedByName = "toId")
    ChatDto toDto(Chat chat);

    @Named("toId")
    default Long toId(User user){
        return user.getId();
    }
}
