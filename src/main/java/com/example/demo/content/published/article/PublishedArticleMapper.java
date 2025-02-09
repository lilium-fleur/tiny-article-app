package com.example.demo.content.published.article;

import com.example.demo.content.published.article.dto.PublishedDto;
import com.example.demo.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PublishedArticleMapper{

    @Mapping(target = "author", source = "author", qualifiedByName = "toUserId")
    PublishedDto toDto(PublishedArticle article);

    @Named("toUserId")
    default Long toUserId(User user){
        return user.getId();
    }

}
