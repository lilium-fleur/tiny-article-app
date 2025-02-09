package com.example.demo.reaction.dislike;

import com.example.demo.content.shared.BaseArticle;
import com.example.demo.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "dislikes",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"article_id", "user_id"}
        )
)
@Entity
public class Dislike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "article_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BaseArticle article;
}

