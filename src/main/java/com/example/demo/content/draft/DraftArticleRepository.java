package com.example.demo.content.draft;

import com.example.demo.content.shared.ArticleRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DraftArticleRepository extends ArticleRepository<DraftArticle> {
}
