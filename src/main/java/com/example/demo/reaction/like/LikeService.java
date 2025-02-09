package com.example.demo.reaction.like;

import com.example.demo.__shared.exception.BadRequestException;
import com.example.demo.content.shared.ArticleService;
import com.example.demo.content.shared.BaseArticle;
import com.example.demo.reaction.dislike.DislikeRepository;
import com.example.demo.reaction.like.dto.LikeDto;
import com.example.demo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;
    private final DislikeRepository dislikeRepository;
    private final ArticleService articleService;

    @Transactional(readOnly = true)
    public List<LikeDto> getAllLikes(Long articleId) {
        return likeRepository.findByArticleId(articleId).stream()
                .map(likeMapper::toDto)
                .toList();
    }

    public LikeDto addLike(Long articleId, User user) {
        BaseArticle article = articleService.getVisibleArticleEntityById(articleId);
        likeRepository.findByUserIdAndArticleId(user.getId(), article.getId())
                .ifPresent(like -> {
                    throw new BadRequestException("User has already liked this article");
                });

        dislikeRepository.findByUserIdAndArticleId(user.getId(), article.getId())
                .ifPresent(dislikeRepository::delete);

        Like like = Like.builder()
                .article(article)
                .user(user)
                .build();
        return likeMapper.toDto(likeRepository.saveAndFlush(like));
    }

    public void removeLike(Long articleId, User user) {
        BaseArticle article = articleService.getVisibleArticleEntityById(articleId);
        Like like = likeRepository.findByUserIdAndArticleId(user.getId(), article.getId())
                .orElseThrow(() -> new BadRequestException("User has not liked this article"));

        likeRepository.delete(like);
    }

}
