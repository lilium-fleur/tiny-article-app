package com.example.demo.chat.message;

import com.example.demo.chat.Chat;
import com.example.demo.chat.message.dto.MessageDto;
import com.example.demo.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "sender", source = "sender", qualifiedByName = "toUserId")
    @Mapping(target = "recipient", source = "recipient", qualifiedByName = "toUserId")
    @Mapping(target = "chat", source = "chat", qualifiedByName = "toChatId")
    MessageDto toDto(Message message);

    @Named("toChatId")
    default Long toChatId(Chat chat){
        return chat.getId();
    }

    @Named("toUserId")
    default Long toUserId(User user){
        return user.getId();
    }
}
