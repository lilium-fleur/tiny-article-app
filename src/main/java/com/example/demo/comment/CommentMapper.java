package com.example.demo.comment;

import com.example.demo.comment.dto.CommentDto;
import com.example.demo.content.shared.BaseArticle;
import com.example.demo.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "replyId", source = "reply", qualifiedByName = "toReplyId")
    @Mapping(target = "authorId", source = "author", qualifiedByName = "toUserId")
    @Mapping(target = "articleId", source = "article", qualifiedByName = "toId")
    CommentDto toDto(Comment comment);

    @Named("toId")
    default Long toArticleId(BaseArticle article){
        return article.getId();
    }

    @Named("toUserId")
    default Long toUserId(User user){
        return user.getId();
    }

    @Named("toReplyId")
    default Long toReplyId(Comment comment){
        if (Objects.isNull(comment)){
            return null;
        }
        return comment.getId();
    }
}
