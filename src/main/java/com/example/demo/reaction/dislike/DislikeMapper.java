package com.example.demo.reaction.dislike;

import com.example.demo.content.shared.BaseArticle;
import com.example.demo.reaction.dislike.dto.DislikeDto;
import com.example.demo.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DislikeMapper {

    @Mapping(target = "userId", source = "user", qualifiedByName = "toUserId")
    @Mapping(target = "articleId", source = "article", qualifiedByName = "toId")
    DislikeDto toDto(Dislike dislike);

    @Named("toUserId")
    default Long toUserId(User user){
        return user.getId();
    }

    @Named("toId")
    default Long toArticleId(BaseArticle article){
        return article.getId();
    }
}
