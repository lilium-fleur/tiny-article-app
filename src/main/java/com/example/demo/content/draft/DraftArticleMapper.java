package com.example.demo.content.draft;

import com.example.demo.content.draft.dto.DraftDto;
import com.example.demo.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DraftArticleMapper{
    @Mapping(target = "author", source = "author", qualifiedByName = "toUserId")
    DraftDto toDto(DraftArticle article);

    @Named("toUserId")
    default Long toUserId(User user){
        return user.getId();
    }
}
