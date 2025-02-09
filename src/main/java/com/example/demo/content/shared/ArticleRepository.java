package com.example.demo.content.shared;

import com.example.demo.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ArticleRepository<T extends BaseArticle> extends JpaRepository<T, Long> {


    Page<T> findAllByAuthorId(Long authorId, Pageable pageable);


    @Query("SELECT a FROM #{#entityName} a " +
            "WHERE a.author = :user " +
            "AND TYPE(a) IN (PublishedArticle, RepostedArticle)" )
    Page<T> findAllArticlesByAuthorId(User user, Pageable pageable);


    @Query("SELECT a FROM #{#entityName} a " +
            "WHERE a.id = :articleId " +
            "AND TYPE(a) IN (PublishedArticle, RepostedArticle)")
    Optional<T> findVisibleArticleById(Long articleId);

    }
















