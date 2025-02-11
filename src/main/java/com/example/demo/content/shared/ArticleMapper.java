package com.example.demo.content.shared;

import com.example.demo.content.published.article.PublishedArticle;
import com.example.demo.content.published.reposted.RepostedArticle;
import org.springframework.stereotype.Component;

@Component
public interface ArticleMapper<T> {
    T mapPublishedArticle(PublishedArticle article);
    T mapRepostedArticle(RepostedArticle article);
}
