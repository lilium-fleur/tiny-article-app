package com.example.demo.content.shared;

import com.example.demo.content.published.article.PublishedArticle;
import com.example.demo.content.published.reposted.RepostedArticle;
import com.example.demo.content.published.reposted.RepostedArticleMapper;
import com.example.demo.content.shared.dto.ArticleDto;
import com.example.demo.content.published.article.PublishedArticleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleMapperImpl implements ArticleMapper<ArticleDto> {
    private final PublishedArticleMapper publishedArticleMapper;
    private final RepostedArticleMapper repostedArticleMapper;


    @Override
    public ArticleDto mapPublishedArticle(PublishedArticle article) {
        return publishedArticleMapper.toDto(article);
    }

    @Override
    public ArticleDto mapRepostedArticle(RepostedArticle article) {
        return repostedArticleMapper.toDto(article);
    }
}
