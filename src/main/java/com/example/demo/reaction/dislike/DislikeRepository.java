package com.example.demo.reaction.dislike;

import com.example.demo.reaction.like.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DislikeRepository extends JpaRepository<Dislike, Long> {

    List<Dislike> findByArticleId(Long articleId);

    Optional<Dislike> findByUserIdAndArticleId(Long userId, Long articleId);
}
