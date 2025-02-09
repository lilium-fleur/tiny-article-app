package com.example.demo.content.published.reposted;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepostedArticleRepository extends JpaRepository<RepostedArticle, Long> {

    Page<RepostedArticle> findAllByOriginalId(Long original, Pageable pageable);

    Page<RepostedArticle> findAllByAuthorId(Long author, Pageable pageable);

}
