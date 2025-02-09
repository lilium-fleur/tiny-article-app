package com.example.demo.reaction.like;

import com.example.demo.content.shared.BaseArticle;
import com.example.demo.reaction.like.dto.LikeDto;
import com.example.demo.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LikeMapper {

    @Mapping(target = "userId", source = "user", qualifiedByName = "toUserId")
    @Mapping(target = "articleId", source = "article", qualifiedByName = "toId")
    LikeDto toDto(Like like);

    @Named("toUserId")
    default Long toUserId(User user){
        return user.getId();
    }

    @Named("toId")
    default Long toArticleId(BaseArticle article){
        return article.getId();
    }
}
