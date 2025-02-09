package com.example.demo.content.shared;

import com.example.demo.content.published.article.PublishedArticle;
import com.example.demo.content.published.reposted.RepostedArticle;
import org.springframework.stereotype.Component;

@Component
public interface ArticleVisitor<T> {
    T visitPublishedArticle(PublishedArticle article);
    T visitRepostedArticle(RepostedArticle article);
}
