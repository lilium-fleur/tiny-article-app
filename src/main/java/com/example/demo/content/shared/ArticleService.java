package com.example.demo.content.shared;

import com.example.demo.content.shared.dto.ArticleDto;
import com.example.demo.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository<BaseArticle> articleRepository;
    private final ArticleMapperImpl mapper;


    @Transactional(readOnly = true)
    public Page<ArticleDto> getVisibleArticlesByUser(User user, Pageable pageable) {
        return articleRepository.findAllVisibleArticlesByAuthorId(user, pageable)
                .map(article -> article.toDto(mapper));
    }

    @Transactional(readOnly = true)
    public BaseArticle getVisibleArticleEntityById(Long articleId){
        return articleRepository.findVisibleArticleById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));
    }


}
