package com.example.demo.content.published.article;


import com.example.demo.content.published.article.dto.CreatePublishedDto;
import com.example.demo.content.published.article.dto.PublishedDto;
import com.example.demo.content.published.article.dto.UpdatePublishedDto;
import com.example.demo.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PublishedArticleService {
    private final PublishedArticleRepository publishedArticleRepository;
    private final PublishedArticleMapper publishedArticleMapper;

    @Transactional(readOnly = true)
    public PublishedArticle getArticleEntityById(Long articleId) {
        return publishedArticleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));
    }

    @Transactional(readOnly = true)
    public PublishedDto getArticleById(Long articleId) {
        PublishedArticle article = publishedArticleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));
        return publishedArticleMapper.toDto(article);
    }

    @Transactional(readOnly = true)
    public Page<PublishedDto> getAllArticlesByUser(Long userId, Pageable pageable) {
        return publishedArticleRepository.findAllByAuthorId(userId, pageable)
                .map(publishedArticleMapper::toDto);
    }

    @Transactional
    public PublishedDto createArticle(CreatePublishedDto createPublishedDto, User user) {
        PublishedArticle article = PublishedArticle.builder()
                .title(createPublishedDto.title())
                .content(createPublishedDto.content())
                .author(user)
                .build();

        PublishedArticle saved = publishedArticleRepository.save(article);

        return publishedArticleMapper.toDto(saved);
    }


    @Transactional
    public void deleteArticleById(Long articleId, User user) {
        PublishedArticle article = getArticleForEditOrThrowException(articleId, user);
        publishedArticleRepository.delete(article);

    }

    @Transactional
    public PublishedDto updateArticle(UpdatePublishedDto updateArticleDto, Long articleId, User user) {
        PublishedArticle article = getArticleForEditOrThrowException(articleId, user);

        Optional.ofNullable(updateArticleDto.title())
                .ifPresent(article::setTitle);
        Optional.ofNullable(updateArticleDto.content())
                .ifPresent(article::setContent);

        return publishedArticleMapper.toDto(publishedArticleRepository.save(article));
    }


    private PublishedArticle getArticleForEditOrThrowException(Long articleId, User user) {
        PublishedArticle article = publishedArticleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));

        if(!article.getAuthor().getId().equals(user.getId())) {
            throw new AccessDeniedException("Permission denied");
        }

        return article;
    }

}

