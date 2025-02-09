package com.example.demo.reaction.dislike;

import com.example.demo.__shared.exception.BadRequestException;
import com.example.demo.content.shared.ArticleService;
import com.example.demo.content.shared.BaseArticle;
import com.example.demo.reaction.dislike.dto.DislikeDto;
import com.example.demo.reaction.like.LikeRepository;
import com.example.demo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DislikeService {
    private final DislikeRepository dislikeRepository;
    private final DislikeMapper dislikeMapper;
    private final LikeRepository likeRepository;
    private final ArticleService articleService;


    @Transactional(readOnly = true)
    public List<DislikeDto> getAllDislikes(Long articleId) {
        return dislikeRepository.findByArticleId(articleId).stream()
                .map(dislikeMapper::toDto)
                .toList();
    }

    @Transactional
    public DislikeDto addDislike(Long articleId, User user) {
        BaseArticle article = articleService.getVisibleArticleEntityById(articleId);

        dislikeRepository.findByUserIdAndArticleId(user.getId(), article.getId())
                .ifPresent(like -> {
                    throw new BadRequestException("User has already disliked this article");
                });

        likeRepository.findByUserIdAndArticleId(user.getId(), article.getId())
                .ifPresent(likeRepository::delete);

        Dislike dislike = Dislike.builder()
                .article(article)
                .user(user)
                .build();
        return dislikeMapper.toDto(dislikeRepository.saveAndFlush(dislike));
    }

    @Transactional
    public void removeDislike(Long articleId, User user) {
        BaseArticle article = articleService.getVisibleArticleEntityById(articleId);
        Dislike dislike = dislikeRepository.findByUserIdAndArticleId(user.getId(), article.getId())
                .orElseThrow(() -> new BadRequestException("User has not disliked this article"));

        dislikeRepository.delete(dislike);
    }
}
