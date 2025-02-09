package com.example.demo.reaction.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findByArticleId(Long articleId);

    Optional<Like> findByUserIdAndArticleId(Long userId, Long articleId);
}
