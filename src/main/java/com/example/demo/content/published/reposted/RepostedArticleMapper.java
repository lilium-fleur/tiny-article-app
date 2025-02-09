package com.example.demo.content.published.reposted;

import com.example.demo.content.published.article.PublishedArticle;
import com.example.demo.content.published.reposted.dto.RepostedDto;
import com.example.demo.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RepostedArticleMapper{

    @Mapping(target = "original", source = "original", qualifiedByName = "toArticleId")
    @Mapping(target = "author", source = "author", qualifiedByName = "toUserId")
    RepostedDto toDto(RepostedArticle article);

    @Named("toArticleId")
    default Long toArticleId(PublishedArticle article){
        return article.getId();
    }

    @Named("toUserId")
    default Long toUserId(User user){
        return user.getId();
    }
}

