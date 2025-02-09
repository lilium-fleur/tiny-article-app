package com.example.demo.comment;

import com.example.demo.content.shared.BaseArticle;
import com.example.demo.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private BaseArticle article;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment reply;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
