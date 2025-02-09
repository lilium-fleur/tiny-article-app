package com.example.demo.content.published.article;

import com.example.demo.content.shared.ArticleRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishedArticleRepository extends ArticleRepository<PublishedArticle>{
}
